package com.sfaxdroid.sky

import android.content.Context
import android.view.MotionEvent
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.renderer.Renderer
import kotlin.math.abs

class WallpaperRenderer(context: Context) : Renderer(context) {

    private var axe: Position = Position.None
    private var downX = 0f
    private var downY = 0f

    override fun onOffsetsChanged(
        v: Float,
        v2: Float,
        v3: Float,
        v4: Float,
        i: Int,
        i2: Int
    ) {
    }

    override fun onTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    downX = event.x
                    downY = event.y
                }
                run {
                    val upX = event.x
                    val upY = event.y
                    val deltaX = downX - upX
                    val deltaY = downY - upY
                    val minDistance = 100
                    if (abs(deltaX) > abs(deltaY)) {
                        if (abs(deltaX) > minDistance) {
                            if (deltaX < 0) {
                                axe = Position.LeftToRights
                            }
                            if (deltaX > 0) {
                                axe = Position.RightToLeft
                            }
                        }
                    } else {
                        if (abs(deltaY) > minDistance) {
                            if (deltaY < 0) {
                                axe = Position.TopToBottom
                            }
                            if (deltaY > 0) {
                                axe = Position.BottomToTop
                            }
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                val upX = event.x
                val upY = event.y
                val deltaX = downX - upX
                val deltaY = downY - upY
                val minDistance = 100
                if (abs(deltaX) > abs(deltaY)) {
                    if (abs(deltaX) > minDistance) {
                        if (deltaX < 0) {
                            axe = Position.LeftToRights
                        }
                        if (deltaX > 0) {
                            axe = Position.RightToLeft
                        }
                    }
                } else {
                    if (abs(deltaY) > minDistance) {
                        if (deltaY < 0) {
                            axe = Position.TopToBottom
                        }
                        if (deltaY > 0) {
                            axe = Position.BottomToTop
                        }
                    }
                }
            }
        }
    }

    override fun onRender(
        ellapsedRealtime: Long,
        deltaTime: Double
    ) {
        super.onRender(ellapsedRealtime, deltaTime)
        rotateCamera()
    }

    private fun rotateCamera() {
        when (axe) {
            is Position.LeftToRights -> currentCamera.rotate(Vector3.Axis.Y, -0.1)
            is Position.RightToLeft -> currentCamera.rotate(Vector3.Axis.Y, +0.1)
            is Position.TopToBottom -> currentCamera.rotate(Vector3.Axis.X, -0.1)
            is Position.BottomToTop -> currentCamera.rotate(Vector3.Axis.X, +0.1)
        }
    }

    override fun initScene() {
        try {
            currentScene.setSkybox(R.drawable.night)
        } catch (ignored: Exception) {
        }
    }

    sealed class Position {
        object LeftToRights : Position()
        object RightToLeft : Position()
        object TopToBottom : Position()
        object BottomToTop : Position()
        object None : Position()
    }

}