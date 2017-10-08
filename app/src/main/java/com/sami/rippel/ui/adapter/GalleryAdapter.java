package com.sami.rippel.ui.adapter;

import android.content.Context;

import com.sami.rippel.allah.R;
import com.sami.rippel.base.BaseRecyclerViewAdapter;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sami.rippel.model.entity.WallpaperObject;

import java.util.List;

public class GalleryAdapter extends BaseRecyclerViewAdapter<WallpaperObject> {

    private TypeCellItemEnum mTypeCell;

    public GalleryAdapter(Context context, List<WallpaperObject> data, TypeCellItemEnum typeCellItemEnum) {
        super(context, data);
        mTypeCell = typeCellItemEnum;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getLayoutId(int viewType) {
        if (mTypeCell == TypeCellItemEnum.GALLERY_CELL) {
            return R.layout.list_item;
        } else if (mTypeCell == TypeCellItemEnum.FRAME_CELL) {
            return R.layout.list_item_frame;
        } else if (mTypeCell == TypeCellItemEnum.CATEGORY_CELL) {
            return R.layout.list_item_frame;
        } else if (mTypeCell == TypeCellItemEnum.STIKERS_CELL) {
            return R.layout.list_item_stickers;
        } else if (mTypeCell == TypeCellItemEnum.CATEGORY_NEW_FORMAT) {
            return R.layout.category_new_item;
        } else if (mTypeCell == TypeCellItemEnum.BITMAP_CELL) {
            return R.layout.list_item;
        } else if (mTypeCell == TypeCellItemEnum.TYPE_ONE)
            return R.layout.list_item_stickers;
        else {
            return R.layout.list_item_frame;
        }
    }

    @Override
    public TypeCellItemEnum getRecycleViewBindType() {
        return mTypeCell;
    }

    public String GetPreviewUrl(int pos) {
        return data.get(pos).getPreviewUrl();
    }

    public String GetFullPictureUrl(int pos) {
        return data.get(pos).getUrl();
    }

    public String GetName(int pos) {
        return data.get(pos).getName();
    }
}
