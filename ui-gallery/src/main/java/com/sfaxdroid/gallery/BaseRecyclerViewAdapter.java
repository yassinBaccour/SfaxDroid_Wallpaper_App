package com.sfaxdroid.gallery;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sfaxdroid.bases.TypeCellItemEnum;
import com.sfaxdroid.base.WallpaperObject;

import java.util.List;


/**
 * Created by yassin baccour on 15/04/2017.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;

    protected List<T> data;

    public BaseRecyclerViewAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
    }

    public abstract int getLayoutId(int viewType);

    public abstract TypeCellItemEnum getRecycleViewBindType();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);  //cherche le type de cellule depuis la classe fille
        if (getRecycleViewBindType() == TypeCellItemEnum.CATEGORY_NEW_FORMAT)
            return new MyItemHolderCategory(view);
        else
            return new MyItemHolderWallpaper(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (data.get(position) instanceof WallpaperObject) {
            WallpaperObject wallpaperObject = (WallpaperObject) data.get(position);
            if (getRecycleViewBindType() == TypeCellItemEnum.CATEGORY_NEW_FORMAT) {
                ((MyItemHolderCategory) holder).bindView(context, wallpaperObject);
            } else
                ((MyItemHolderWallpaper) holder).bindView(context, wallpaperObject);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
