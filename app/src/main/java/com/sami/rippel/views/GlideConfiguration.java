package com.sami.rippel.views;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.RequestOptions;

public class GlideConfiguration extends AppGlideModule {

	@Override
	public void applyOptions(Context mContext, GlideBuilder mBuilder) {
        mBuilder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));
	}
}
