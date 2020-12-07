package com.sfaxdoird.anim.word;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.sfaxdroid.base.SharedPrefsUtils;

import java.io.File;

public class AnimWord2dWallpaper extends WallpaperService {

    private static int currentPhoto = 1;
    private Paint mPaintOption;

    @Override
    public Engine onCreateEngine() {
        SharedPrefsUtils pref = new SharedPrefsUtils(this);
        int color = pref.GetSetting("DouaLwpColor", -4522170);
        int textSize = pref.GetSetting("nameofallahtextsize", 1) * 20;
        int fontStyle = pref.GetSetting("nameofallahfontstyle", 1);
        return new IslamicEngine(color, textSize, fontStyle);
    }

    class IslamicEngine extends Engine {
        private final Handler mHandler = new Handler();
        private final SurfaceHolder mHolder = getSurfaceHolder();
        private File mFilesDir = getExternalFilesDir("");
        private int mScreenHeight;
        private int mScreenWidth;
        private Bitmap mBackground;
        private boolean mVisible;
        private final Runnable mDrawPattern = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private BitmapFactory.Options mOptions = new BitmapFactory.Options();
        private int mColor;

        IslamicEngine(int color, int textSize, int fontStyle) {
            mColor = color;
            final Paint mPaint = new Paint();
            mPaint.setColor(0xffffffff);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(2);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStyle(Paint.Style.STROKE);
            mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

            mPaintOption = new Paint();
            mPaintOption.setColor(mColor);

            mPaintOption.setTextSize(textSize * getResources().getDisplayMetrics().density);
            if (getTypeFace(fontStyle) != null)
                mPaintOption.setTypeface(getTypeFace(fontStyle));
        }


        Typeface getTypeFace(int mTypefaceNum) {
            try {
                if (mTypefaceNum == 1)
                    return Typeface.createFromAsset(getAssets(), "arabicfont1.otf");
                if (mTypefaceNum == 2)
                    return Typeface.createFromAsset(getAssets(), "arabicfont2.ttf");
                else if (mTypefaceNum == 3)
                    return Typeface.createFromAsset(getAssets(), "arabicfont3.ttf");
                else if (mTypefaceNum == 4)
                    return Typeface.createFromAsset(getAssets(), "arabicfont4.otf");
                else if (mTypefaceNum == 5)
                    return Typeface.createFromAsset(getAssets(), "arabicfont5.ttf");
                else if (mTypefaceNum == 6)
                    return Typeface.createFromAsset(getAssets(), "arabicfont6.ttf");
                else if (mTypefaceNum == 7)
                    return Typeface.createFromAsset(getAssets(), "arabicfont7.ttf");
                else
                    return Typeface.createFromAsset(getAssets(), "arabicfont8.ttf");
            } catch (Exception e) {
                return Typeface.DEFAULT;
            }
        }


        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawPattern);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawPattern);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            if (mBackground != null) {
                mBackground.recycle();
                mBackground = null;
            }
            Bitmap mBtm = BitmapFactory.decodeFile(mFilesDir + "/" + com.sfaxdroid.base.Constants.DOUA_PNG_BACKFROUND_FILE_NAME,
                    mOptions);
            if (mBtm != null) {
                mBackground = Bitmap.createScaledBitmap(mBtm, width, height, true);
                mBtm.recycle();
            }
            if (mBackground != null)
                drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawPattern);
            if (mBackground != null)
                mBackground.recycle();
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
                                     float yStep, int xPixels, int yPixels) {
            drawFrame();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                com.sfaxdroid.base.Constants.nbIncrementationAfterChange = com.sfaxdroid.base.Constants.nbIncrementationAfterChange + 1;
                if (mScreenWidth > 0
                        && com.sfaxdroid.base.Constants.ifBackgroundChanged
                        && com.sfaxdroid.base.Constants.nbIncrementationAfterChange == 5) {
                    com.sfaxdroid.base.Constants.nbIncrementationAfterChange = 0;
                    com.sfaxdroid.base.Constants.ifBackgroundChanged = false;
                    mBackground = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(mFilesDir + "/"
                                    + com.sfaxdroid.base.Constants.DOUA_PNG_BACKFROUND_FILE_NAME, mOptions),
                            mScreenWidth, mScreenHeight, true);
                }

                //todo maybe change color
                mPaintOption.setColor(mColor);

                if (currentPhoto == 98) {
                    currentPhoto = 0;
                }
                currentPhoto++;
            }
            super.onTouchEvent(event);
        }

        void drawFrame() {
            Canvas canvas = null;
            try {
                canvas = mHolder.lockCanvas();
                if (canvas != null) {
                    drawPattern(canvas);
                }
            } finally {
                if (canvas != null)
                    mHolder.unlockCanvasAndPost(canvas);
            }
            mHandler.removeCallbacks(mDrawPattern);
            if (mVisible) {
                mHandler.postDelayed(mDrawPattern, 1000 / 25);
            }
        }

        void drawPattern(Canvas c) {
            if (mBackground != null && mBackground.getWidth() != 0) {
                c.save();
                c.drawColor(0xff000000);
                mScreenWidth = c.getWidth();
                mScreenHeight = c.getHeight();
                if (!mBackground.isRecycled()) {
                    c.drawBitmap(mBackground, 0, 0, null);
                }
                String header = Constants.NAME_OF_ALLAH_TAB.get(currentPhoto);
                Rect r = new Rect();
                c.getClipBounds(r);
                int cHeight = r.height();
                int cWidth = r.width();
                mPaintOption.setTextAlign(Paint.Align.LEFT);
                mPaintOption.getTextBounds(header, 0, header.length(), r);
                float x = cWidth / 2f - r.width() / 2f - r.left;
                float y = cHeight / 2f + r.height() / 2f - r.bottom;
                c.drawText(header, x, y, mPaintOption);
                c.restore();
            }
        }
    }
}