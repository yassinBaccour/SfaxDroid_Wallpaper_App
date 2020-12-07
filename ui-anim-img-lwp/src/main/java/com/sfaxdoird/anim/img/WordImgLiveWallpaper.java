package com.sfaxdoird.anim.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.sfaxdroid.base.BitmapUtils;
import com.sfaxdroid.base.Constants;
import com.sfaxdroid.base.SharedPrefsUtils;

import java.io.File;

public class WordImgLiveWallpaper extends WallpaperService {

    public static int mCurrentPhoto = 1;

    @Override
    public Engine onCreateEngine() {
        SharedPrefsUtils pref = new SharedPrefsUtils(this);
        int color = pref.GetSetting("DouaLwpColor", -4522170);
        File tempDir = Utils.getTemporaryDouaDir(this, com.sfaxdoird.anim.img.Constants.KEY_DOUA_FOLDER_CONTAINER, getString(R.string.app_namenospace));
        String backgroundFileName = Constants.DOUA_PNG_BACKFROUND_FILE_NAME;
        String zipFolder = Constants.DOUA_ZIP_FOLDER_NAME;
        return new ImgWordEngine(color, tempDir, backgroundFileName, zipFolder);
    }

    private class ImgWordEngine extends Engine {
        private final SurfaceHolder mHolder = getSurfaceHolder();
        private final Handler mHandler = new Handler();
        private BitmapFactory.Options mOptions = new BitmapFactory.Options();
        private int mScreenHeight;
        private int mScreenWidth;
        private Bitmap background;
        private Bitmap word;
        private boolean mVisible;
        private final Runnable mDrawPattern = this::drawFrame;
        private int mColor;
        private File tempDir;
        private String zipFolder;
        private String backgroundFileName;
        private Paint mPaintOption;

        ImgWordEngine(int color, File tempDir, String backgroundFileName, String zipFolder) {
            mColor = color;
            this.tempDir = tempDir;
            this.backgroundFileName = backgroundFileName;
            this.zipFolder = zipFolder;
            final Paint mPaint = new Paint();
            mPaint.setColor(0xffffffff);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(2);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStyle(Paint.Style.STROKE);
            mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            word = BitmapUtils.changeImageColor(BitmapFactory.decodeFile(tempDir + "/" + zipFolder + "/" + getPrefix() + mCurrentPhoto + ".png",
                    mOptions), mColor);
            mPaintOption = new Paint();
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
            if (background != null) {
                background.recycle();
                background = null;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(tempDir + "/" + backgroundFileName,
                    mOptions);
            if (bitmap != null) {
                background = Bitmap.createScaledBitmap(bitmap, width, height, true);
                bitmap.recycle();
            }
            if (background != null)
                drawFrame();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawPattern);
            if (background != null)
                background.recycle();
            if (word != null)
                word.recycle();
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
                                     float yStep, int xPixels, int yPixels) {
            drawFrame();
        }

        private String getPrefix() {
            if (mCurrentPhoto < 10) {
                return "i_000";
            } else {
                return "i_00";
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Constants.nbIncrementationAfterChange = Constants.nbIncrementationAfterChange + 1;
                if (mScreenWidth > 0 && Constants.ifBackgroundChanged && Constants.nbIncrementationAfterChange == 5) {
                    Constants.nbIncrementationAfterChange = 0;
                    Constants.ifBackgroundChanged = false;
                    background = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(tempDir + "/" + backgroundFileName,
                            mOptions), mScreenWidth, mScreenHeight, true);
                }
                if (word != null) {
                    word.recycle();
                }
                //TODO maybe change color
                word = BitmapUtils.changeImageColor(BitmapFactory.decodeFile(tempDir + "/" + zipFolder + "/" + getPrefix() + mCurrentPhoto + ".png",
                        mOptions), mColor);
                if (mCurrentPhoto == 58) {
                    mCurrentPhoto = 0;
                }
                mCurrentPhoto++;
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

        void drawPattern(Canvas canvas) {
            if (word != null && word.getWidth() != 0 && background != null && background.getWidth() != 0) {
                canvas.save();
                canvas.drawColor(0xff000000);
                mScreenWidth = canvas.getWidth();
                mScreenHeight = canvas.getHeight();
                if (!background.isRecycled()) {
                    canvas.drawBitmap(background, 0, 0, null);
                }
                int debut = mScreenWidth / 2 - word.getWidth() / 2;
                int fin = mScreenHeight / 2 - word.getHeight() / 2;
                canvas.drawBitmap(word, debut, fin, mPaintOption);
                canvas.restore();
            }
        }
    }
}