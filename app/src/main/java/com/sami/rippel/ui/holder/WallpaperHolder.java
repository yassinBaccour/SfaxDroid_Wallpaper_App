package com.sami.rippel.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sami.rippel.allah.R;
import com.sami.rippel.base.BaseHolder;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.views.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by yassin baccour on 15/04/2017.
 */

//J'utilise plus le holder commun
public class WallpaperHolder extends BaseHolder<WallpaperObject> {
    private ImageView mImg;

    public WallpaperHolder(View itemView) {
        super(itemView);
        mImg = (ImageView) itemView.findViewById(R.id.item_img);
    }

    @Override
    public void setData(Context context, WallpaperObject wall) {

        GlideApp.with(context)
                .load(GetUrlByScreen(wall))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .override(ViewModel.Current.device.getCellWidht(), ViewModel.Current.device.getCellHeight())
                .transition(withCrossFade())
                .into(mImg);
    }

    private String GetUrlByScreen(WallpaperObject wall) {
        return ViewModel.Current.getUrlFromWallpaper(wall);
    }

    @Override
    public void onClick(View view) {

    }

}
