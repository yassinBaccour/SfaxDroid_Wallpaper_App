package com.sami.rippel.livewallpapers.lwpskyview;

import android.content.Context;
import android.view.MotionEvent;

import com.sami.rippel.allah.R;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;

public class WallpaperRenderer extends Renderer {

    private String axe = "";
    private float downX;
    private float downY;

    public WallpaperRenderer(Context context) {
        super(context);
    }

    @Override
    public void onOffsetsChanged(float v, float v2, float v3, float v4, int i, int i2) {
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
            }
            case MotionEvent.ACTION_UP: {
                float upX = event.getX();
                float upY = event.getY();
                float deltaX = downX - upX;
                float deltaY = downY - upY;
                int min_distance = 100;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > min_distance) {
                        // left or right
                        if (deltaX < 0) {
                            axe = "LefttoRigh";
                        }
                        if (deltaX > 0) {
                            axe = "RighToLeft";
                        }
                    }
                }
                //VERTICAL SCROLL
                else {
                    if (Math.abs(deltaY) > min_distance) {
                        // top or down
                        if (deltaY < 0) {
                            axe = "TopToBottom";
                        }
                        if (deltaY > 0) {
                            axe = "BottomToTop";
                        }
                    }
                }
            }
        }
    }


    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        try {
            switch (axe) {
                case "LefttoRigh":
                    getCurrentCamera().rotate(Vector3.Axis.Y, -0.1);
                    break;
                case "RighToLeft":
                    getCurrentCamera().rotate(Vector3.Axis.Y, +0.1);
                    break;
                case "TopToBottom":
                    getCurrentCamera().rotate(Vector3.Axis.X, -0.1);
                    break;
                case "BottomToTop":
                    getCurrentCamera().rotate(Vector3.Axis.X, +0.1);
                    break;
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void initScene() {
        //Material mAtlasMaterial = new Material();
        try {
            getCurrentScene().setSkybox(R.drawable.night); //FIXME BITMAP TO TEXTURE
        } catch (Exception ignored) {
        }
        /*
        try {
            Bitmap vvv = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.n_70);
        int Texture [] = { R.drawable.n_70 };
        mAtlasMaterial.addTexture(new Texture("eee", vvv));
        mAtlasMaterial.setColorInfluence(0);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        Plane plane2 = new Plane();
        plane2.setTransparent(true);
        plane2.setMaterial(mAtlasMaterial);
        plane2.setPosition(.2f, .2f, 2);
        getCurrentScene().addChild(plane2);
        */
    }
}
