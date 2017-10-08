package com.sami.rippel.base;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sami.rippel.allah.R;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sami.rippel.model.entity.WallpaperObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yassin baccour on 15/04/2017.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    //yassin classe abstraite pour l'adapter et pour le holder, avec type generique T
    protected Context context;
    protected List<T> data = new ArrayList<>();
    public abstract int getLayoutId(int viewType);
    public abstract TypeCellItemEnum getRecycleViewBindType();

    public BaseRecyclerViewAdapter(Context context, List<T> data)
    {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);  //cherche le type de cellule depuis la classe fille
        if (getRecycleViewBindType() == TypeCellItemEnum.CATEGORY_NEW_FORMAT)
        return new MyItemHolderCategory(view, context);
        else
            return new MyItemHolderWallpaper(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (data.get(position) instanceof WallpaperObject)
        {
            WallpaperObject wallpaperObject = (WallpaperObject)data.get(position);
        if (getRecycleViewBindType() == TypeCellItemEnum.CATEGORY_NEW_FORMAT)
        {
            Glide.with(context).load(GetUrlByScreen(wallpaperObject))
                    .thumbnail(0.5f)
                    .override(ViewModel.Current.device.getCellWidht(), ViewModel.Current.device.getCellHeight())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((MyItemHolderCategory) holder).mImg);

            ((MyItemHolderCategory) holder).ln.setBackgroundColor(Color.parseColor(wallpaperObject.getColor()));
            ((MyItemHolderCategory) holder).title.setText(wallpaperObject.getName());
            ((MyItemHolderCategory) holder).desc.setText(wallpaperObject.getDesc());
        }else
        Glide.with(context).load(GetUrlByScreen(wallpaperObject))
                .thumbnail(0.5f)
                .override(ViewModel.Current.device.getCellWidht(), ViewModel.Current.device.getCellHeight())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((MyItemHolderWallpaper) holder).mImg);
    }}

    @Override
    public int getItemCount() {
        return 0;
    }

    private String GetUrlByScreen(WallpaperObject wall)
    {
        return ViewModel.Current.getUrlFromWallpaper(wall);
    }

    private static class MyItemHolderWallpaper extends RecyclerView.ViewHolder {
        ImageView mImg;
        MyItemHolderWallpaper(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }


    private static class MyItemHolderCategory extends RecyclerView.ViewHolder {
        ImageView mImg;
        LinearLayout ln;
        TextView title, desc;
        Context context;

        MyItemHolderCategory(View itemView, Context context) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
            ln  = (LinearLayout) itemView.findViewById(R.id.contentImg);
            title = (TextView) itemView.findViewById(R.id.title);
            desc  = (TextView) itemView.findViewById(R.id.subtitle);
            this.context = context;
        }
    }
}
