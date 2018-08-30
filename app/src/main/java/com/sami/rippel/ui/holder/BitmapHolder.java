package com.sami.rippel.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sami.rippel.allah.R;
import com.sami.rippel.base.BaseHolder;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.views.GlideApp;

import java.io.File;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by yassin baccour on 15/04/2017.
 */
//J'utilise plus le holder commun
public class BitmapHolder extends BaseHolder<File> {

    private ImageView mImg;

    public BitmapHolder(View itemView) {
        super(itemView);
        mImg = itemView.findViewById(R.id.item_img);
    }

    @Override
    public void setData(Context context, File wall) {
        GlideApp.with(context).load(wall.getPath())
                .thumbnail(0.5f)
                .override(ViewModel.Current.device.getCellWidht(), ViewModel.Current.device.getCellHeight())
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImg);
    }

    @Override
    public void onClick(View view) {
    }
}
