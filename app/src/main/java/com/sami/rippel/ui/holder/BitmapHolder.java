package com.sami.rippel.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sami.rippel.allah.R;
import com.sami.rippel.base.BaseHolder;
import com.sami.rippel.model.ViewModel;

import java.io.File;

/**
 * Created by yassin baccour on 15/04/2017.
 */
//J'utilise plus le holder commun
public class BitmapHolder extends BaseHolder<File> {
    public ImageView mImg;

    public BitmapHolder(View itemView) {
        super(itemView);
        mImg = (ImageView) itemView.findViewById(R.id.item_img);
    }

    @Override
    public void setData(Context context, File wall) {
        Glide.with(context).load(wall.getPath())
                .thumbnail(0.5f)
                .override(ViewModel.Current.device.getCellWidht(), ViewModel.Current.device.getCellHeight())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImg);
    }

    @Override
    public void onClick(View view) {
    }
}
