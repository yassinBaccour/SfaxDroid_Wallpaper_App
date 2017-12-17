package com.sami.rippel.livewallpapers.lwpskyview;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.rajawali3d.renderer.ISurfaceRenderer;
import org.rajawali3d.view.ISurface;
import org.rajawali3d.wallpaper.Wallpaper;

import java.lang.reflect.InvocationTargetException;

public class RajawaliExampleWallpaper extends Wallpaper {

    private ISurfaceRenderer mRenderer;

    @Override
    public Engine onCreateEngine() {
        final SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useFallback = false;
        try {
            final Class rendererClass = Class.forName(mSharedPreferences.getString("renderer_class", WallpaperRenderer.class.getCanonicalName()));
            mRenderer = (ISurfaceRenderer) rendererClass.getConstructor(Context.class).newInstance(this);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            useFallback = true;
        }
        if (useFallback) mRenderer = new WallpaperRenderer(this);
        return new WallpaperEngine(getBaseContext(), mRenderer, ISurface.ANTI_ALIASING_CONFIG.NONE);
    }
}
