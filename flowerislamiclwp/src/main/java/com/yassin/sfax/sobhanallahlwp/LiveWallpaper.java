package com.yassin.sfax.sobhanallahlwp;

import java.util.ArrayList;
import com.yassin.sfax.sobhanallahlwp.R;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.PictureDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class LiveWallpaper extends WallpaperService {
	public static final String SHARED_PREFS_NAME = "livewallpapersettings";

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new TestPatternEngine();
	}

	class TestPatternEngine extends Engine {
		private final Handler mHandler = new Handler();
		private float mTouchX = -1;
		private float mTouchY = -1;
		private final Paint mPaint = new Paint();
		private final Runnable mDrawPattern = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		ArrayList<Cloud> clouds;
		int screen_height;
		int screen_width;
		int new_cloud_iteration = 0;
		int current_chuy = 0;
		Bitmap background, cloud, chuy1, chuy2, chuy3, chuy4;
		final SurfaceHolder holder = getSurfaceHolder();
		int max_width, max_height;
		private boolean mVisible;
		GradientDrawable mGradient;

		TestPatternEngine() {
			RecycleBitmap();
			final Paint paint = mPaint;
			paint.setColor(0xffffffff);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(2);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStyle(Paint.Style.STROKE);

			clouds = new ArrayList<Cloud>();
			clouds.add(new Cloud(10, 50));
			// mision= diversas resoluciones
			// max_height = holder.getSurfaceFrame().height();
			// max_width = holder.getSurfaceFrame().width();

			// charger le ciel
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.sbh1);
			// cloud c'est les nuages
			cloud = BitmapFactory.decodeResource(getResources(),
					R.drawable.sbh1t);

			chuy1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.sbh1t);
			chuy2 = BitmapFactory.decodeResource(getResources(),
					R.drawable.sbh1t);
			chuy3 = BitmapFactory.decodeResource(getResources(),
					R.drawable.sbh1t);
			chuy4 = BitmapFactory.decodeResource(getResources(),
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
			// handler supprime le runnable
			mHandler.removeCallbacks(mDrawPattern);

		}

		@Override
		public void onVisibilityChanged(boolean visible) {

			SharedPreferences sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(LiveWallpaper.this);
			boolean Anim1 = sharedPrefs.getBoolean("checkBox1", true);
			boolean Anim2 = sharedPrefs.getBoolean("checkBox2", true);

			if (sharedPrefs.getString("prefSyncFrequency", "NULL").equals(
					"Style 18")) {
				background = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh1);
				cloud = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh1t);
				chuy1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh1t);
				chuy2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh1t);
				chuy3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh1t);
				chuy4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh1t);

				if (Anim1 == false) {
					cloud = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
				if (Anim2 == false) {
					chuy1 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy2 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy3 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy4 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}

			}

			else if (sharedPrefs.getString("prefSyncFrequency", "NULL").equals(
					"Style 19")) {
				background = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2);
				cloud = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);
				chuy1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);
				chuy2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);
				chuy3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);
				chuy4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);

				if (Anim1 == false) {
					cloud = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
				if (Anim2 == false) {
					chuy1 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy2 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy3 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy4 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}

			} else if (sharedPrefs.getString("prefSyncFrequency", "NULL")
					.equals("Style 20")) {
				background = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh3);
				cloud = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh3t);
				chuy1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh3t);
				chuy2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh3t);
				chuy3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh3t);
				chuy4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh3t);

				if (Anim1 == false) {
					cloud = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
				if (Anim2 == false) {
					chuy1 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy2 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy3 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy4 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
			} else if (sharedPrefs.getString("prefSyncFrequency", "NULL")
					.equals("Style 21")) {
				background = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh4);
				cloud = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);
				chuy1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);
				chuy2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);
				chuy3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);
				chuy4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh2t);

				if (Anim1 == false) {
					cloud = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
				if (Anim2 == false) {
					chuy1 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy2 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy3 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy4 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
			} else if (sharedPrefs.getString("prefSyncFrequency", "NULL")
					.equals("Style 22")) {
				background = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh5);
				cloud = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh5t);
				chuy1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh5t);
				chuy2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh5t);
				chuy3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh5t);
				chuy4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh5t);

				if (Anim1 == false) {
					cloud = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
				if (Anim2 == false) {
					chuy1 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy2 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy3 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy4 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
			} else if (sharedPrefs.getString("prefSyncFrequency", "NULL")
					.equals("Style 23")) {
				background = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh6);
				cloud = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh6t);
				chuy1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh6t);
				chuy2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh6t);
				chuy3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh6t);
				chuy4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbh6t);

				if (Anim1 == false) {
					cloud = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
				if (Anim2 == false) {
					chuy1 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy2 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy3 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
					chuy4 = BitmapFactory.decodeResource(getResources(),
							R.drawable.none);
				}
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
			if (cloud != null)
				cloud.recycle();

			if (chuy1 != null)
				chuy1.recycle();

			if (chuy2 != null)
				chuy2.recycle();

			if (chuy3 != null)
				chuy3.recycle();

			if (chuy4 != null)
				chuy4.recycle();

			if (background != null)
				background.recycle();
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mTouchX = event.getX();
				mTouchY = event.getY();
			} else {
			}
			super.onTouchEvent(event);
		}

		void drawFrame() {
			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					drawPattern(c);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}
			mHandler.removeCallbacks(mDrawPattern);
			if (mVisible) {
				mHandler.postDelayed(mDrawPattern, 1000 / 25);
			}
		}

		void drawPattern(Canvas c) {
			c.save();
			c.drawColor(0xff000000);
			screen_width = c.getWidth();
			screen_height = c.getHeight();
			if (new_cloud_iteration > 200 + (int) (Math.random() * 1000 % 100)) {
				clouds.add(new Cloud(
						(int) (Math.random() * 1000 % screen_width), 0));
				new_cloud_iteration = 0;
			}
			new_cloud_iteration++;
			c.drawBitmap(background, 0, 0, null);

			for (int i = 0; i < clouds.size(); i++) {
				c.drawBitmap(cloud, clouds.get(i).x - cloud.getWidth() / 2,
						clouds.get(i).y - cloud.getHeight() / 2, null);
				clouds.get(i).y++;
				if (clouds.get(i).y > screen_height) {
					clouds.remove(i);
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