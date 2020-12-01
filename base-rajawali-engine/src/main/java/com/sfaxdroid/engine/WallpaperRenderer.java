package com.sfaxdroid.engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.filters.TouchRippleFilter;
import rajawali.lights.DirectionalLight;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureManager.TextureType;
import rajawali.primitives.Plane;
import rajawali.renderer.PostProcessingRenderer.PostProcessingQuality;
import rajawali.renderer.RajawaliRenderer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;

public class WallpaperRenderer extends RajawaliRenderer {

    private MediaPlayer mediaPlayer;
    private TouchRippleFilter mFilter;
    private Point mScreenSize;
    private long frameCount;
    private boolean changingBG = false;
    private boolean enableSound = false;
    private float rippleSize = 96;
    private float rippleSpeed = 4;
    private int wallpaperResource;
    private int soundId;

    public WallpaperRenderer(Context context, int soundId, int wallpaperResource) {
        super(context);
        this.mContext = context;
        this.soundId = soundId;
        this.wallpaperResource = wallpaperResource;
        initSound(context);
        setFrameRate(60);
    }

    private void initSound(Context context) {
        try {
            AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_ALARM);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(
                    soundId);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getDeclaredLength());
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initScene() {
        mCamera.setPosition(0, 0, -10);
        DirectionalLight light = new DirectionalLight(0, 0, 1);
        light.setPower(1f);
        SimpleMaterial planeMat = new SimpleMaterial();
        Bitmap texture = BitmapFactory.decodeResource(mContext.getResources(),
                wallpaperResource);
        planeMat.addTexture(mTextureManager.addTexture(texture,
                TextureType.DIFFUSE));
        Plane plane = new Plane(11, 6.30f, 1, 1);
        plane.setRotZ(-90);
        plane.setMaterial(planeMat);
        addChild(plane);
        mFilter = new TouchRippleFilter();
        mFilter.setRippleSize(rippleSize);
        mFilter.setRippleSpeed(rippleSpeed);
        mPostProcessingRenderer.setQuadSegments(40);
        mPostProcessingRenderer.setQuality(PostProcessingQuality.HIGH);
        addPostProcessingFilter(mFilter);
    }

    public void initBackground(int resource) {
        wallpaperResource = resource;
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
            }
            if (enableSound) {
                if (mediaPlayer == null) {
                    AudioManager audioManager = (AudioManager) mContext
                            .getSystemService(Context.AUDIO_SERVICE);
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM,
                            maxVolume, 0);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    AssetFileDescriptor afd = mContext.getResources()
                            .openRawResourceFd(soundId);
                    mediaPlayer.setDataSource(afd.getFileDescriptor(),
                            afd.getStartOffset(), afd.getDeclaredLength());
                    mediaPlayer.prepare();
                }
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }
        } catch (Exception ignored) {
        }
        super.onTouchEvent(event);
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
