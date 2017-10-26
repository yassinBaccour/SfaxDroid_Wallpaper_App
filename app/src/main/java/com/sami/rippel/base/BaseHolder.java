package com.sami.rippel.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yassin baccour on 15/04/2017.
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void setData(Context context, T data);
}
