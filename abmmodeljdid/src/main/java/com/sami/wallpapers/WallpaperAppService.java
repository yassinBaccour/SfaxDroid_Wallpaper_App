package com.sami.wallpapers;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.view.animation.DecelerateInterpolator;

import java.util.Locale;

public class WallpaperAppService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    class WallpaperEngine extends Engine {
        private static final int FRAME_MAX_DIGIT = 99;
        private int mCurrentPosition = 0;
        private int mSpeed = 10000;
        private final Handler mHandler = new Handler();
        private final Matrix mTransformationMatrix = new Matrix();
        private final BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
        private final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(1f);
        private int mScreenWidth;
        private int mScreenHeight;
        private int mTickCount = 0;
        private final Runnable mDrawRunnable = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(WallpaperAppService.this);
            if (sharedPrefs.getString("prefquality", "none").equalsIgnoreCase("quality1")) {
                mBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            } else {
                mBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            }
            mBitmapOptions.inPurgeable = true;
            mBitmapOptions.inInputShareable = true;
        }

        @Override
        public void onVisibilityChanged(final boolean visible) {
            setSpeedFromPref();
            if (visible) {
                draw();
            } else {
                mHandler.removeCallbacks(mDrawRunnable);
            }
        }

        private void setSpeedFromPref() {
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(WallpaperAppService.this);
            String speed = sharedPrefs.getString("prefSyncFrequency", "");
            switch (speed) {
                case "speed1":
                    mSpeed = 300000;
                    break;
                case "speed2":
                    mSpeed = 20000;
                    break;
                case "speed3":
                    mSpeed = 10000;
                    break;
                case "speed4":
                    mSpeed = 8000;
                    break;
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            mScreenWidth = width;
            mScreenHeight = height;
        }

        void draw() {
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            mHandler.removeCallbacks(mDrawRunnable);
            if (mTickCount == 0) {
                startWithTime(mDrawRunnable, mSpeed);
            } else {
                startWithTime(mDrawRunnable, (long) (mSpeed * mDecelerateInterpolator
                        .getInterpolation(mTickCount
                                / ((float) FRAME_MAX_DIGIT))));
            }
        }

        private void startWithTime(Runnable runnable, long time) {
            mHandler.postDelayed(mDrawRunnable, time);
        }

        private void draw(final Canvas c) {

            if (mTickCount > FRAME_MAX_DIGIT) {
                mTickCount = 0;
            }

            mCurrentPosition++;
            if (mCurrentPosition >= FRAME_MAX_DIGIT) {
                mCurrentPosition = 0;
            }

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("img_"
                                    + String.format("%05d", mCurrentPosition % FRAME_MAX_DIGIT, Locale.US), "drawable",
                            getPackageName()),
                    mBitmapOptions);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, mScreenWidth,
                    mScreenHeight, true);
            c.drawBitmap(scaledBitmap, mTransformationMatrix, null);
            bitmap.recycle();
            mTickCount++;
        }
    }
}
