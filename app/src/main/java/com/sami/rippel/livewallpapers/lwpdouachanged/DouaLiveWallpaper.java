package com.sami.rippel.livewallpapers.lwpdouachanged;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;

import java.io.File;

public class DouaLiveWallpaper extends WallpaperService {
    public static int mCurrentPhoto = 1;
    private Paint mPaintOption;

    @Override
    public Engine onCreateEngine() {
        return new IslamicEngine();
    }

    private class IslamicEngine extends Engine {
        private final SurfaceHolder mHolder = getSurfaceHolder();
        private final Handler mHandler = new Handler();
        private final Paint mPaint = new Paint();
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        private float mTouchX = -1;
        private float mTouchY = -1;
        private File mFilesDir = getExternalFilesDir("");
        private int mScreenHeight;
        private int mScreenWidth;
        private Bitmap mBackground;
        private Bitmap mLogoDoua;
        private boolean mVisible;
        private final Runnable mDrawPattern = this::drawFrame;
        private int mColor;

        IslamicEngine() {
            mColor = ViewModel.Current.sharedPrefsUtils.GetSetting("DouaLwpColor", -4522170);
            final Paint mPaint = this.mPaint;
            mPaint.setColor(0xffffffff);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(2);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStyle(Paint.Style.STROKE);
            mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            mLogoDoua = ViewModel.Current.bitmapUtils.changeImageColor(BitmapFactory.decodeFile(mFilesDir + "/" + Constants.PNG_ZIP_DOUA_EXTRACTED_FOLDER + "/" + getPrefix() + mCurrentPhoto + ".png",
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
            if (mBackground != null) {
                mBackground.recycle();
                mBackground = null;
            }
            Bitmap btm = BitmapFactory.decodeFile(mFilesDir + "/" + Constants.DOUA_PNG_BACKFROUND_FILE_NAME,
                    mOptions);
            if (btm != null) {
                mBackground = Bitmap.createScaledBitmap(btm, width, height, true);
                btm.recycle();
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
            if (mLogoDoua != null)
                mLogoDoua.recycle();
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
                Constants.nb_incrementation_after_change = Constants.nb_incrementation_after_change + 1;
                if (mScreenWidth > 0 && mScreenWidth > 0 && Constants.ifBackground_changed && Constants.nb_incrementation_after_change == 5) {
                    Constants.nb_incrementation_after_change = 0;
                    Constants.ifBackground_changed = false;
                    mBackground = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(mFilesDir + "/" + Constants.DOUA_PNG_BACKFROUND_FILE_NAME,
                            mOptions), mScreenWidth, mScreenHeight, true);
                }
                mTouchX = event.getX();
                mTouchY = event.getY();
                if (mLogoDoua != null) {
                    mLogoDoua.recycle();
                }
                mColor = ViewModel.Current.sharedPrefsUtils.GetSetting("DouaLwpColor", -4522170);
                mLogoDoua = ViewModel.Current.bitmapUtils.changeImageColor(BitmapFactory.decodeFile(mFilesDir + "/" + Constants.PNG_ZIP_DOUA_EXTRACTED_FOLDER + "/" + getPrefix() + mCurrentPhoto + ".png",
                        mOptions), mColor);
                if (mCurrentPhoto == 58) {
                    mCurrentPhoto = 0;
                }
                mCurrentPhoto++;
            }
            super.onTouchEvent(event);
        }

        void drawFrame() {
            Canvas c = null;
            try {
                c = mHolder.lockCanvas();
                if (c != null) {
                    drawPattern(c);
                }
            } finally {
                if (c != null)
                    mHolder.unlockCanvasAndPost(c);
            }
            mHandler.removeCallbacks(mDrawPattern);
            if (mVisible) {
                mHandler.postDelayed(mDrawPattern, 1000 / 25);
            }
        }

        void drawPattern(Canvas c) {
            if (mLogoDoua != null && mLogoDoua.getWidth() != 0 && mBackground != null && mBackground.getWidth() != 0) {
                c.save();
                c.drawColor(0xff000000);
                mScreenWidth = c.getWidth();
                mScreenHeight = c.getHeight();
                if (!mBackground.isRecycled()) {
                    c.drawBitmap(mBackground, 0, 0, null);
                }
                int debut = mScreenWidth / 2 - mLogoDoua.getWidth() / 2;
                int fin = mScreenHeight / 2 - mLogoDoua.getHeight() / 2;
                c.drawBitmap(mLogoDoua, debut, fin, mPaintOption);
                c.restore();
                //Constants.ifBackground_changed = false;
            }
        }
    }
}