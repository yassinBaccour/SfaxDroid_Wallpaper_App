package com.sfaxdroid.engine

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Point
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.MotionEvent
import rajawali.filters.TouchRippleFilter
import rajawali.lights.DirectionalLight
import rajawali.materials.SimpleMaterial
import rajawali.materials.TextureManager.TextureType
import rajawali.primitives.Plane
import rajawali.renderer.PostProcessingRenderer.PostProcessingQuality
import rajawali.renderer.RajawaliRenderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class WallpaperRenderer(context: Context, soundId: Int, wallpaperResource: Int) :
    RajawaliRenderer(context) {
    private var mediaPlayer: MediaPlayer? = null
    private var mFilter: TouchRippleFilter? = null
    private var mScreenSize: Point? = null
    private var frameCount: Long = 0
    private val changingBG = false
    private var enableSound = false
    private var rippleSize = 96f
    private var rippleSpeed = 4f
    private var wallpaperResource: Int
    private val soundId: Int

    init {
        mContext = context
        this.soundId = soundId
        this.wallpaperResource = wallpaperResource
        initSound(context)
        frameRate = 60
    }

    private fun initSound(context: Context) {
        try {
            val audioManager = context
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager
                .getStreamMaxVolume(AudioManager.STREAM_ALARM)
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0)
            mediaPlayer = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_ALARM)
                val afd = context.resources.openRawResourceFd(
                    soundId
                )
                setDataSource(
                    afd.fileDescriptor, afd.startOffset,
                    afd.declaredLength
                )
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public override fun initScene() {
        mCamera.setPosition(0f, 0f, -10f)
        val light = DirectionalLight(0f, 0f, 1f)
        light.power = 1f
        val planeMat = SimpleMaterial()
        val texture = BitmapFactory.decodeResource(
            mContext.resources,
            wallpaperResource
        )
        planeMat.addTexture(
            mTextureManager.addTexture(
                texture,
                TextureType.DIFFUSE
            )
        )

        addChild(
            Plane(11f, 6.30f, 1, 1).apply {
                rotZ = -90f
                material = planeMat
            }
        )

        mPostProcessingRenderer.quadSegments = 40
        mPostProcessingRenderer.quality = PostProcessingQuality.HIGH

        mFilter = TouchRippleFilter().apply {
            rippleSize = rippleSize
            rippleSpeed = rippleSpeed
        }
        addPostProcessingFilter(mFilter)
    }

    fun initBackground(resource: Int) {
        wallpaperResource = resource
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        super.onSurfaceCreated(gl, config)
        mFilter?.setScreenSize(mViewportWidth.toFloat(), mViewportHeight.toFloat())
    }

    override fun onDrawFrame(glUnused: GL10) {
        super.onDrawFrame(glUnused)
        mFilter?.setTime(frameCount++.toFloat() * .05f)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        mFilter?.setScreenSize(width.toFloat(), height.toFloat())
        mScreenSize = Point()
        mScreenSize?.x = width
        mScreenSize?.y = height
    }

    override fun onTouchEvent(event: MotionEvent) {
        try {
            if (!changingBG && event.action == MotionEvent.ACTION_DOWN) {
                mFilter?.addTouch(
                    event.x / mScreenSize!!.x,
                    1.0f - event.y / mScreenSize!!.y,
                    frameCount * .05f
                )
            }
            if (enableSound) {
                if (mediaPlayer == null) {
                    val audioManager = mContext
                        .getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
                    audioManager.setStreamVolume(
                        AudioManager.STREAM_ALARM,
                        maxVolume, 0
                    )
                    mediaPlayer = MediaPlayer().apply {
                        setAudioStreamType(AudioManager.STREAM_ALARM)
                        val afd = mContext.resources
                            .openRawResourceFd(soundId)
                        setDataSource(
                            afd.fileDescriptor,
                            afd.startOffset, afd.declaredLength
                        )
                        prepare()
                    }
                }
                if (mediaPlayer?.isPlaying == false) mediaPlayer?.start()
            }
        } catch (ignored: Exception) {
        }
        super.onTouchEvent(event)
    }

    fun setSound(on: Boolean) {
        enableSound = on
    }

    fun setRippleSize(`val`: Float) {
        mFilter?.rippleSize = `val`
        rippleSize = `val`
    }

    fun setRippleSpeed(`val`: Float) {
        mFilter?.rippleSpeed = `val`
        rippleSpeed = `val`
    }
}
