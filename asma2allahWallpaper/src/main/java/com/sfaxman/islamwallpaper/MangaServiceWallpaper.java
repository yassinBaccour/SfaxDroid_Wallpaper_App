package com.sfaxman.islamwallpaper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
 
public class MangaServiceWallpaper extends WallpaperService {
	private static final String TAG = MangaServiceWallpaper.class.getSimpleName();

    @Override
    public Engine onCreateEngine() {
      return new WallpaperEngine();
    	}
    private enum SwipeDirection {None, Left, Right}
    
    class WallpaperEngine extends Engine   {
        private static final int FRAME_MAX_DIGIT = 60;
        int i=0;
        int xxx=10000;
        int j =0;
        private final Handler mHandler = new Handler();
        private final Matrix mTransformationMatrix = new Matrix();
        private final BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
        private final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(1f);
        private final Runnable mDrawRunnable = new Runnable() 
		        {
		       @Override
		       public void run() {draw();}
		        };
  
        private int mScreenWidth;
        private int mScreenHeight;
        private int mTickCount = 0;
        String imagename="islam1_";
 
        
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
             mBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
             mBitmapOptions.inPurgeable = true;
             mBitmapOptions.inInputShareable = true;  
        }

       

        @Override
        public void onVisibilityChanged(final boolean visible) {
        	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MangaServiceWallpaper.this);
        	if(sharedPrefs.getString("prefSyncFrequency","ddd").equals("Style 1"))
             {
             	 xxx=20000;
             } 
        	 else if(sharedPrefs.getString("prefSyncFrequency","ddd").equals("Style 2"))
             {
             	 xxx=5000;
             }
        	 else if(sharedPrefs.getString("prefSyncFrequency","ddd").equals("Style 3"))
             {
             	 xxx=3000;
             }
        	 else if(sharedPrefs.getString("prefSyncFrequency","ddd").equals("Style 4"))
             {
             	 xxx=80000;
             }
        	if (visible) 
        	{
        	draw();
        	}
        	else
        	{mHandler.removeCallbacks(mDrawRunnable);} 	
        }
  
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,int width, int height) {
            super.onSurfaceChanged(holder, format, width, height); 
            mScreenWidth = width;
            mScreenHeight = height;  
        }
         
        public void draw() {     	
           final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {draw(c);}} finally {if (c != null) {holder.unlockCanvasAndPost(c);}}
            mHandler.removeCallbacks(mDrawRunnable);
            if (mTickCount == 0) {	
            	mHandler.postDelayed(mDrawRunnable, xxx);       	
            } else {       	
            	mHandler.postDelayed(mDrawRunnable,(long) (xxx * mDecelerateInterpolator.getInterpolation(mTickCount / ((float) FRAME_MAX_DIGIT))));
            	    }
           }
        
            private void draw(final Canvas c)
            {	
            	i++;
            	if(i>=FRAME_MAX_DIGIT)
            	{i=0;}
            	
            	if(i<10)
            	{
            		imagename = "islam1_";
            		String drawableName = imagename + String.format("%05d", i % FRAME_MAX_DIGIT, Locale.US);
            		int resId = getResources().getIdentifier(drawableName,"drawable", getPackageName());
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), resId, mBitmapOptions);
                    if (bm != null && !bm.isRecycled())
                    {
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, mScreenWidth,mScreenHeight, true);
                    c.drawBitmap(scaledBitmap, mTransformationMatrix, null);
                    bm.recycle();
                    mTickCount++;
                    }
            	}
            	else if(i>=10&&i<20)
            	{
            		
            		if(i==10)
            		{
            			j = 0;
            		}
            		imagename = "islam2_";
            		String drawableName = imagename + String.format("%05d", j % FRAME_MAX_DIGIT, Locale.US);
            		int resId = getResources().getIdentifier(drawableName,"drawable", getPackageName());
                    Log.d(null, "Drawable Nameeeeeeeeeeeeeeee =  "+drawableName);
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), resId, mBitmapOptions);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, mScreenWidth,mScreenHeight, true);
                    c.drawBitmap(scaledBitmap, mTransformationMatrix, null);
                    bm.recycle();
                    j++;
                    mTickCount++;
            	}
            	else if(i>=20&&i<30)
            	{		
            		if(i==20)
            		{
            			j = 0;
            		}
            		imagename = "islam3_";
            		String drawableName = imagename + String.format("%05d", j % FRAME_MAX_DIGIT, Locale.US);

            		int resId = getResources().getIdentifier(drawableName,"drawable", getPackageName());
                    Log.d(null, "Drawable Nameeeeeeeeeeeeeeee =  "+drawableName);
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), resId, mBitmapOptions);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, mScreenWidth,mScreenHeight, true);
                    c.drawBitmap(scaledBitmap, mTransformationMatrix, null);
                    bm.recycle();
                    j++;
                    mTickCount++;
            	}
            	else if(i>=30&&i<40)
            	{
            		
            		if(i==30)
            		{
            			j = 0;
            		}
            		imagename = "islam4_";
            		String drawableName = imagename + String.format("%05d", j % FRAME_MAX_DIGIT, Locale.US);

            		int resId = getResources().getIdentifier(drawableName,"drawable", getPackageName());
                    Log.d(null, "Drawable Nameeeeeeeeeeeeeeee =  "+drawableName);
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), resId, mBitmapOptions);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, mScreenWidth,mScreenHeight, true);
                    c.drawBitmap(scaledBitmap, mTransformationMatrix, null);
                    bm.recycle();
                    j++;
                    mTickCount++;
            	}
            	else if(i>=40&&i<50)
            	{
            		
            		if(i==40)
            		{
            			j = 0;
            		}
            		imagename = "islam5_";
            		String drawableName = imagename + String.format("%05d", j % FRAME_MAX_DIGIT, Locale.US);

            		int resId = getResources().getIdentifier(drawableName,"drawable", getPackageName());
                    Log.d(null, "Drawable Nameeeeeeeeeeeeeeee =  "+drawableName);
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), resId, mBitmapOptions);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, mScreenWidth,mScreenHeight, true);
                    c.drawBitmap(scaledBitmap, mTransformationMatrix, null);
                    bm.recycle();
                    j++;
                    mTickCount++;
            	}
            	else if(i>=50&&i<60)
            	{
            		
            		if(i==50)
            		{
            			j = 0;
            		}
            		imagename = "islam6_";
            		String drawableName = imagename + String.format("%05d", j % FRAME_MAX_DIGIT, Locale.US);
            		int resId = getResources().getIdentifier(drawableName,"drawable", getPackageName());
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), resId, mBitmapOptions);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, mScreenWidth,mScreenHeight, true);
                    c.drawBitmap(scaledBitmap, mTransformationMatrix, null);
                    bm.recycle();
                    j++;
                    mTickCount++;
            	}
                
            }		
    }
}
