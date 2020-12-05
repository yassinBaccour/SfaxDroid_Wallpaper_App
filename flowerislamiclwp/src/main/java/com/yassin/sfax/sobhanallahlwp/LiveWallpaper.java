package com.yassin.sfax.sobhanallahlwp;

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
        return new TestPatternEngine();
    }

    class TestPatternEngine extends Engine {
        private final Handler mHandler = new Handler();
        private float mTouchX = -1;
        private float mTouchY = -1;
        private final Runnable mDrawPattern = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private final ArrayList<AnimatedItem> wallpaperItems;
        private int new_cloud_iteration = 0;
        private int currentItem = 0;
        private Bitmap background, img0, img1, img2, img3, img4;
        private final SurfaceHolder holder = getSurfaceHolder();
        private boolean mVisible;

        TestPatternEngine() {
            RecycleBitmap();
            final Paint paint = new Paint();
            paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            wallpaperItems = new ArrayList<>();
            wallpaperItems.add(new AnimatedItem(10, 50));
            background = BitmapFactory.decodeResource(getResources(),
                    R.drawable.wallpaper_one);
            img0 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.sbh1t);
            img1 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.sbh1t);
            img2 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.sbh1t);
            img3 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.sbh1t);
            img4 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.sbh1t);
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
            switch (sharedPrefs.getString("prefSyncFrequency", "")) {
                case "Style 18":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.wallpaper_one);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh1t);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh1t);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh1t);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh1t);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh1t);

                    if (!Anim1) {
                        img0 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        img1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }

                    break;
                case "Style 19":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.wallpaper_two);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);

                    if (!Anim1) {
                        img0 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        img1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }

                    break;
                case "Style 20":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.wallpaper_three);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh3t);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh3t);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh3t);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh3t);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh3t);

                    if (!Anim1) {
                        img0 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        img1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 21":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.wallpaper_four);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh2t);

                    if (!Anim1) {
                        img0 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        img1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 22":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.wallpaper_five);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh5t);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh5t);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh5t);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh5t);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh5t);

                    if (!Anim1) {
                        img0 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        img1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 23":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh6);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh6t);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh6t);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh6t);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh6t);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.sbh6t);

                    if (!Anim1) {
                        img0 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        img1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        img4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
            }

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

        private void RecycleBitmap() {
            if (img0 != null)
                img0.recycle();

            if (img1 != null)
                img1.recycle();

            if (img2 != null)
                img2.recycle();

            if (img3 != null)
                img3.recycle();

            if (img4 != null)
                img4.recycle();

            if (background != null)
                background.recycle();
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
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawPattern(canvas);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            mHandler.removeCallbacks(mDrawPattern);
            if (mVisible) {
                mHandler.postDelayed(mDrawPattern, 1000 / 25);
            }
        }

        void drawPattern(Canvas c) {
            c.save();
            c.drawColor(0xff000000);
            int screen_width = c.getWidth();
            int screen_height = c.getHeight();
            if (new_cloud_iteration > 200 + (int) (Math.random() * 1000 % 100)) {
                wallpaperItems.add(new AnimatedItem(
                        (int) (Math.random() * 1000 % screen_width), 0));
                new_cloud_iteration = 0;
            }
            new_cloud_iteration++;
            c.drawBitmap(background, 0, 0, null);

            for (int i = 0; i < wallpaperItems.size(); i++) {
                c.drawBitmap(img0, wallpaperItems.get(i).x - img0.getWidth() / 2,
                        wallpaperItems.get(i).y - img0.getHeight() / 2, null);
                wallpaperItems.get(i).y++;
                if (wallpaperItems.get(i).y > screen_height) {
                    wallpaperItems.remove(i);
                    i--;
                }
            }
            if (mTouchX >= 0 && mTouchY >= 0) {
                if (currentItem == 0) {
                    c.drawBitmap(img1, mTouchX - img1.getWidth() / 2, mTouchY
                            - img1.getHeight() / 2, null);
                } else if (currentItem == 1) {
                    c.drawBitmap(img2, mTouchX - img2.getWidth() / 2, mTouchY
                            - img2.getHeight() / 2, null);
                } else if (currentItem == 2) {
                    c.drawBitmap(img3, mTouchX - img3.getWidth() / 2, mTouchY
                            - img3.getHeight() / 2, null);
                } else if (currentItem == 3) {
                    c.drawBitmap(img4, mTouchX - img4.getWidth() / 2, mTouchY
                            - img4.getHeight() / 2, null);
                }
                currentItem++;
                if (currentItem >= 4)
                    currentItem = 0;
            }
            c.restore();
        }
    }
}