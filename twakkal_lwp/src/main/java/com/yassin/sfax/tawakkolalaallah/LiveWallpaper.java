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
        private int currentItem = 0;
        private Bitmap background, img0, img1, img2, img3, img4;
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
                    R.drawable.tw1);
            img0 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
            img1 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
            img2 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
            img3 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tw41);
            img4 = BitmapFactory.decodeResource(getResources(),
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
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
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
                            R.drawable.tw2);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
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
                            R.drawable.tw3);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
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
                            R.drawable.tw4);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw41);
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
                            R.drawable.tw5);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
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
                            R.drawable.tw6);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw61);
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
                case "Style 24":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw7);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
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
                case "Style 25":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw8);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);

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
                case "Style 26":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw9);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
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
                case "Style 27":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw10);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);

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
                case "Style 28":
                    background = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw11);
                    img0 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img2 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img3 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);
                    img4 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tw21);

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
            canvas.drawColor(0xff000000);
            int screenWidth = canvas.getWidth();
            int screenHeight = canvas.getHeight();
            if (newCloudIteration > 200 + (int) (Math.random() * 1000 % 100)) {
                animatedItems.add(new AnimatedItem(
                        (int) (Math.random() * 1000 % screenWidth), 0));
                newCloudIteration = 0;
            }
            newCloudIteration++;
            canvas.drawBitmap(background, 0, 0, null);
            for (int i = 0; i < animatedItems.size(); i++) {
                canvas.drawBitmap(img0, animatedItems.get(i).x - img0.getWidth() / 2,
                        animatedItems.get(i).y - img0.getHeight() / 2, null);
                animatedItems.get(i).y++;
                if (animatedItems.get(i).y > screenHeight) {
                    animatedItems.remove(i);
                    i--;
                }
            }
            if (mTouchX >= 0 && mTouchY >= 0) {
                if (currentItem == 0) {
                    canvas.drawBitmap(img1, mTouchX - img1.getWidth() / 2, mTouchY
                            - img1.getHeight() / 2, null);
                } else if (currentItem == 1) {
                    canvas.drawBitmap(img2, mTouchX - img2.getWidth() / 2, mTouchY
                            - img2.getHeight() / 2, null);
                } else if (currentItem == 2) {
                    canvas.drawBitmap(img3, mTouchX - img3.getWidth() / 2, mTouchY
                            - img3.getHeight() / 2, null);
                } else if (currentItem == 3) {
                    canvas.drawBitmap(img4, mTouchX - img4.getWidth() / 2, mTouchY
                            - img4.getHeight() / 2, null);
                }
                currentItem++;
                if (currentItem >= 4)
                    currentItem = 0;
            }
            canvas.restore();
        }
    }
}
