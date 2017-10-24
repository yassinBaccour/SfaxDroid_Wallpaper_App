package com.sami.rippel.livewallpapers.lwpskyview;

import android.content.Context;
import android.view.MotionEvent;

import com.sami.rippel.allah.R;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;

public class WallpaperRenderer extends Renderer {

    String axe = "";
    private float downX, downY, upX, upY;
    private int min_distance = 100;

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
                upX = event.getX();
                upY = event.getY();
                float deltaX = downX - upX;
                float deltaY = downY - upY;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > min_distance) {
                        // left or right
                        if (deltaX < 0) {
                            axe = "LefttoRigh";
                        }
                        if (deltaX > 0) {
                            axe = "RighToLeft";
                        }
                    } else {
                        //not long enough swipe...
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
                    } else {
                        //not long enough swipe...
                    }
                }
            }
        }
    }


    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        try {
            if (axe.equals("LefttoRigh"))
                getCurrentCamera().rotate(Vector3.Axis.Y, -0.1);
            else if (axe.equals("RighToLeft"))
                getCurrentCamera().rotate(Vector3.Axis.Y, +0.1);
            else if (axe.equals("TopToBottom"))
                getCurrentCamera().rotate(Vector3.Axis.X, -0.1);
            else if (axe.equals("BottomToTop"))
                getCurrentCamera().rotate(Vector3.Axis.X, +0.1);
        } catch (Exception e) {
        }
    }

    @Override
    protected void initScene() {
        //Material mAtlasMaterial = new Material();
        try {
            getCurrentScene().setSkybox(R.drawable.night); //FIXME BITMAP TO TEXTURE
        } catch (Exception e) {
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
