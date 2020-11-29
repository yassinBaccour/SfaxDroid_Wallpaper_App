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
        private final ArrayList<AnimatedItem> animatedItems;
        private int new_cloud_iteration = 0;
        private int current_chuy = 0;
        private Bitmap background, cloud, chuy1, chuy2, chuy3, chuy4;
        final SurfaceHolder holder = getSurfaceHolder();
        private boolean mVisible;

        TestPatternEngine() {
            final Paint paint = new Paint();
            paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            animatedItems = new ArrayList<>();
            animatedItems.add(new AnimatedItem(10, 50));
            background = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw1);
            cloud = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
            chuy1 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
            chuy2 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
            chuy3 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
            chuy4 = BitmapFactory.decodeResource(getResources(),
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
            switch (sharedPrefs.getString("prefSyncFrequency", "NULL")) {
                case "Style 18":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw1);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 19":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw2);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 20":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw3);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 21":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw4);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 22":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw5);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 23":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw6);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 24":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw7);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 25":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw8);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);

                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 26":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw9);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 27":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw10);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);

                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    break;
                case "Style 28":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw11);
                    cloud = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    chuy4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);

                    if (!Anim1) {
                        cloud = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                    }
                    if (!Anim2) {
                        chuy1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy2 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy3 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.none);
                        chuy4 = BitmapFactory.decodeResource(getResources(),
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
                animatedItems.add(new AnimatedItem(
                        (int) (Math.random() * 1000 % screen_width), 0));
                new_cloud_iteration = 0;
            }
            new_cloud_iteration++;
            c.drawBitmap(background, 0, 0, null);
            for (int i = 0; i < animatedItems.size(); i++) {
                c.drawBitmap(cloud, animatedItems.get(i).x - cloud.getWidth() / 2,
                        animatedItems.get(i).y - cloud.getHeight() / 2, null);
                animatedItems.get(i).y++;
                if (animatedItems.get(i).y > screen_height) {
                    animatedItems.remove(i);
                    i--;
                }
            }
            if (mTouchX >= 0 && mTouchY >= 0) {
                if (current_chuy == 0) {
                    c.drawBitmap(chuy1, mTouchX - chuy1.getWidth() / 2, mTouchY
                            - chuy1.getHeight() / 2, null);
                } else if (current_chuy == 1) {
                    c.drawBitmap(chuy2, mTouchX - chuy2.getWidth() / 2, mTouchY
                            - chuy2.getHeight() / 2, null);
                } else if (current_chuy == 2) {
                    c.drawBitmap(chuy3, mTouchX - chuy3.getWidth() / 2, mTouchY
                            - chuy3.getHeight() / 2, null);
                } else if (current_chuy == 3) {
                    c.drawBitmap(chuy4, mTouchX - chuy4.getWidth() / 2, mTouchY
                            - chuy4.getHeight() / 2, null);
                }
                current_chuy++;
                if (current_chuy >= 4)
                    current_chuy = 0;
            }
            c.restore();
        }
    }
}
