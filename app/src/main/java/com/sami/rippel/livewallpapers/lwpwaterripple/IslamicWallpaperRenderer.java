package com.sami.rippel.livewallpapers.lwpwaterripple;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rajawali.filters.TouchRippleFilter;
import rajawali.lights.DirectionalLight;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureInfo;
import rajawali.materials.TextureManager.TextureType;
import rajawali.primitives.Plane;
import rajawali.renderer.PostProcessingRenderer.PostProcessingQuality;
import rajawali.renderer.RajawaliRenderer;

public class IslamicWallpaperRenderer extends RajawaliRenderer {
    private final int QUAD_SEGMENTS = 40;
    private TouchRippleFilter mFilter;
    private long frameCount;
    private Point mScreenSize;
    private boolean enableSound = false;
    private float rippleSize = 96;
    private float rippleSpeed = 4;
    private MediaPlayer mp;
    private BitmapFactory.Options options = new BitmapFactory.Options();

    public IslamicWallpaperRenderer(Context context) {
        super(context);
        InitSound(context);
        this.mContext = context;
        setFrameRate(60);
    }

    private void InitSound(Context context) {
        try {
            AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_ALARM);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(
                    R.raw.water_drop);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getDeclaredLength());
            mp.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Flowable<Bitmap> LoadTexture(Bitmap myBackground) {

        return Flowable.just(myBackground).flatMap(btm -> Flowable.fromCallable(() ->
        {
            if (btm != null) {
                return Bitmap.createScaledBitmap(btm, ViewModel.Current.device.getScreenWidthPixels(), ViewModel.Current.device.getScreenHeightPixels(), true);
            }
            return null;
        }));
    }

    public void initScene() {
        Flowable.fromCallable(() -> BitmapFactory.decodeFile(Constants.FilePath))
                .flatMap(this::LoadTexture)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmapTexture -> {
                    if (mTextureManager != null && bitmapTexture != null) {
                        TextureInfo txtInfo = mTextureManager.addTexture(bitmapTexture,
                                TextureType.DIFFUSE);
                        if (txtInfo != null) {
                            try {
                                mCamera.setPosition(0, 0, -10);
                                DirectionalLight light = new DirectionalLight(0, 0, 1);
                                light.setPower(1f);
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                SimpleMaterial planeMat = new SimpleMaterial();
                                planeMat.addTexture(txtInfo);
                                Plane plane = new Plane(11, 6.30f, 1, 1);
                                plane.setRotZ(-90);
                                plane.setMaterial(planeMat);
                                addChild(plane);
                                mFilter = new TouchRippleFilter();
                                mFilter.setRippleSize(rippleSize);
                                mFilter.setRippleSpeed(rippleSpeed);
                                mPostProcessingRenderer.setQuadSegments(QUAD_SEGMENTS);
                                mPostProcessingRenderer.setQuality(PostProcessingQuality.HIGH);
                                addPostProcessingFilter(mFilter);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }, throwable -> {

                });
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            super.onSurfaceCreated(gl, config);
            mFilter.setScreenSize(mViewportWidth, mViewportHeight);
        } catch (Exception ignored) {
        }
    }

    public void onDrawFrame(GL10 glUnused) {
        try {
            super.onDrawFrame(glUnused);
            mFilter.setTime((float) frameCount++ * .05f);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onSurfaceDestroyed() {
        super.onSurfaceDestroyed();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        try {
            super.onSurfaceChanged(gl, width, height);
            mFilter.setScreenSize(width, height);
            mScreenSize = new Point();
            mScreenSize.x = width;
            mScreenSize.y = height;
        } catch (Exception ignored) {
        }

    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        try {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mFilter.addTouch(event.getX() / mScreenSize.x,
                        1.0f - (event.getY() / mScreenSize.y),
                        frameCount * .05f);
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
        } catch (Exception ignored) {
        }
        super.onTouchEvent(event);
    }

    void setSound(boolean on) {
        enableSound = on;
    }

    void setRippleSize(float val) {
        if (mFilter != null)
            mFilter.setRippleSize(val);
        rippleSize = val;
    }

    void setRippleSpeed(float val) {
        if (mFilter != null)
            mFilter.setRippleSpeed(val);
        rippleSpeed = val;
    }
}
