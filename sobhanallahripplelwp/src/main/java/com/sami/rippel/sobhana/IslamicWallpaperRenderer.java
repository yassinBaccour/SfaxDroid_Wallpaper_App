package com.sami.rippel.sobhana;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.sami.rippel.sobhana.R;

import rajawali.filters.TouchRippleFilter;
import rajawali.lights.DirectionalLight;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureManager.TextureType;
import rajawali.primitives.Plane;
import rajawali.renderer.PostProcessingRenderer.PostProcessingQuality;
import rajawali.renderer.RajawaliRenderer;
import rajawali.wallpaper.Wallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

public class IslamicWallpaperRenderer extends RajawaliRenderer {
    private TouchRippleFilter mFilter;
    private long frameCount;
    private Point mScreenSize;
    private Plane plane;
    private boolean changingBG = false;
    private boolean enableSound = false;
    private float rippleSize = 96;
    private float rippleSpeed = 4;
    private MediaPlayer mp;

    public IslamicWallpaperRenderer(Context context) {
        super(context);
        try {
            AudioManager amanager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = amanager
                    .getStreamMaxVolume(AudioManager.STREAM_ALARM);
            amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(
                    R.raw.water_drop);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getDeclaredLength());
            mp.prepareAsync();
            this.mContext = context;
            setFrameRate(60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initScene() {
        mCamera.setPosition(0, 0, -10);

        DirectionalLight light = new DirectionalLight(0, 0, 1);
        light.setPower(1f);

        SimpleMaterial planeMat = new SimpleMaterial();
        Bitmap texture;
        texture = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.wall2);
        planeMat.addTexture(mTextureManager.addTexture(texture,
                TextureType.DIFFUSE));
        Plane plane = new Plane(11, 6.30f, 1, 1);
        plane.setRotZ(-90);
        plane.setMaterial(planeMat);
        addChild(plane);
        mFilter = new TouchRippleFilter();
        mFilter.setRippleSize(rippleSize);
        mFilter.setRippleSpeed(rippleSpeed);
        int QUAD_SEGMENTS = 40;
        mPostProcessingRenderer.setQuadSegments(QUAD_SEGMENTS);
        mPostProcessingRenderer.setQuality(PostProcessingQuality.HIGH);
        addPostProcessingFilter(mFilter);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        mFilter.setScreenSize(mViewportWidth, mViewportHeight);
    }

    public void onDrawFrame(GL10 glUnused) {
        super.onDrawFrame(glUnused);
        mFilter.setTime((float) frameCount++ * .05f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        mFilter.setScreenSize(width, height);
        mScreenSize = new Point();
        mScreenSize.x = width;
        mScreenSize.y = height;
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        try {
            if (!changingBG && event.getAction() == MotionEvent.ACTION_DOWN) {
                mFilter.addTouch(event.getX() / mScreenSize.x,
                        1.0f - (event.getY() / mScreenSize.y),
                        frameCount * .05f);
                // MediaPlayer.create(mContext, R.raw.ic_sound_effect).start();
            }
            if (enableSound) {
                if (mp == null) {
                    AudioManager amanager = (AudioManager) mContext
                            .getSystemService(Context.AUDIO_SERVICE);
                    int maxVolume = amanager
                            .getStreamMaxVolume(AudioManager.STREAM_ALARM);
                    amanager.setStreamVolume(AudioManager.STREAM_ALARM,
                            maxVolume, 0);
                    mp = new MediaPlayer();
                    mp.setAudioStreamType(AudioManager.STREAM_ALARM);
                    AssetFileDescriptor afd = mContext.getResources()
                            .openRawResourceFd(R.raw.water_drop);
                    mp.setDataSource(afd.getFileDescriptor(),
                            afd.getStartOffset(), afd.getDeclaredLength());
                    mp.prepare();
                }
                if (!mp.isPlaying())
                    mp.start();
            }
        } catch (Exception e) {
        }
        super.onTouchEvent(event);
    }

    public void changeBackground() {
        changingBG = true;
        removeChild(plane);
        SimpleMaterial planeMat = new SimpleMaterial();
        Bitmap texture = null;
        try {
            texture = BitmapFactory.decodeFile(mContext.getFilesDir()
                    + "/img.jpg");
        } catch (Exception e) {
        }
        if (texture == null)
            texture = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.wall2);
        planeMat.addTexture(mTextureManager.addTexture(texture,
                TextureType.DIFFUSE));
        Plane plane = new Plane(11, 6.30f, 1, 1);
        plane.setRotZ(-90);
        plane.setMaterial(planeMat);
        addChild(plane);
        changingBG = false;
    }

    public void setSound(boolean on) {
        enableSound = on;
    }

    public void setRippleSize(float val) {
        if (mFilter != null)
            mFilter.setRippleSize(val);
        rippleSize = val;
    }

    public void setRippleSpeed(float val) {
        if (mFilter != null)
            mFilter.setRippleSpeed(val);
        rippleSpeed = val;
    }

}
