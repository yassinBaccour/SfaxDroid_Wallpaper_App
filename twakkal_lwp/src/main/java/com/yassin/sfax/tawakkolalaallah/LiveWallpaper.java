package com.yassin.sfax.tawakkolalaallah;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.sfaxdroid.base.AnimatedItem;

public class LiveWallpaper extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new WallpaperPatternEngine();
    }

    class WallpaperPatternEngine extends Engine {
        private final Handler mHandler = new Handler();
        private float mTouchX = -1;
        private float mTouchY = -1;
        private final Runnable mDrawPattern = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private final ArrayList<AnimatedItem> animatedItems;
        private int newCloudIteration = 0;
        private Bitmap background, rainingItem, touchingItem;
        final SurfaceHolder surfaceHolder = getSurfaceHolder();
        private boolean mVisible;

        WallpaperPatternEngine() {
            final Paint paint = new Paint();
            paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            animatedItems = new ArrayList<>();
            animatedItems.add(new AnimatedItem(10, 50));
            background = BitmapFactory.decodeResource(getResources(),
                    R.drawable.wallpaper1);
            rainingItem = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
            touchingItem = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
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

            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(LiveWallpaper.this);

            boolean Anim1 = sharedPrefs.getBoolean("checkBox1", true);
            boolean Anim2 = sharedPrefs.getBoolean("checkBox2", true);

            String syncFreq = sharedPrefs.getString("prefSyncFrequency", "NULL");

            background = ResourceUtils.Companion.getBackground(getBaseContext(), syncFreq);
            rainingItem = ResourceUtils.Companion.getRainingItem(getBaseContext(), syncFreq, Anim1);
            touchingItem = ResourceUtils.Companion.getTouchingItem(getBaseContext(), syncFreq, Anim2);

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
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
                                     float yStep, int xPixels, int yPixels) {
            drawFrame();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = event.getX();
                mTouchY = event.getY();
            }
            super.onTouchEvent(event);
        }

        void drawFrame() {
            Canvas canvas = null;
            try {

                canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    drawPattern(canvas);
                }
            } finally {
                if (canvas != null)
                    surfaceHolder.unlockCanvasAndPost(canvas);
            }

            mHandler.removeCallbacks(mDrawPattern);
            if (mVisible) {
                mHandler.postDelayed(mDrawPattern, 1000 / 25);
            }
        }

        void drawPattern(Canvas canvas) {
            canvas.save();
            drawBackground(canvas);
            drawRainingItem(canvas);
            drawTouchingItem(canvas);
        }

        private void drawBackground(Canvas canvas) {
            canvas.drawColor(0xff000000);
            canvas.drawBitmap(background, 0, 0, null);
        }

        private void drawRainingItem(Canvas canvas) {
            int screenWidth = canvas.getWidth();
            if (newCloudIteration > 200 + (int) (Math.random() * 1000 % 100)) {
                animatedItems.add(new AnimatedItem(
                        (int) (Math.random() * 1000 % screenWidth), 0));
                newCloudIteration = 0;
            }
            newCloudIteration++;
            int screenHeight = canvas.getHeight();
            for (int i = 0; i < animatedItems.size(); i++) {
                canvas.drawBitmap(rainingItem, animatedItems.get(i).x - rainingItem.getWidth() / 2,
                        animatedItems.get(i).y - rainingItem.getHeight() / 2, null);
                animatedItems.get(i).y++;
                if (animatedItems.get(i).y > screenHeight) {
                    animatedItems.remove(i);
                    i--;
                }
            }
        }

        private void drawTouchingItem(Canvas canvas) {
            if (mTouchX >= 0 && mTouchY >= 0) {
                canvas.drawBitmap(touchingItem, mTouchX - touchingItem.getWidth() / 2, mTouchY
                        - touchingItem.getHeight() / 2, null);
                canvas.restore();
            }
        }


    }
}
