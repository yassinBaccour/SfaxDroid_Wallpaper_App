package com.sami.rippel.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sami.rippel.allah.R;
import com.sami.rippel.base.BasePagerAdapter;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.ui.activity.DetailsActivity;
import com.sami.rippel.views.TouchImageView;

import java.util.ArrayList;

public class DetailPagerAdapter extends BasePagerAdapter<WallpaperObject> {
    private Context mContext;
    private int mResourceId;

    public DetailPagerAdapter(Context context, int resourceId,
                              ArrayList<WallpaperObject> data) {
        super(data);
        this.mContext = context;
        this.mResourceId = resourceId;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final WallpaperObject item = getItem(position);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(mResourceId, container, false);
        final TouchImageView mDetailImage = (TouchImageView) viewLayout
                .findViewById(R.id.detailImage);
        TouchImageView img = new TouchImageView(container.getContext());
        final ProgressBar mProgressBar = (ProgressBar) viewLayout
                .findViewById(R.id.progressBar);
        Glide.with(mContext).load(GetUrlByScreen(item.getUrl()))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource,
                                                Transition<? super Drawable> glideAnimation) {
                        mDetailImage.setImageDrawable(resource);
                        mProgressBar.setVisibility(View.GONE);
                        ((DetailsActivity) mContext).hideProgressBar();
                    }
                });
        container.addView(viewLayout);
        container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        return viewLayout;
    }

    private String GetUrlByScreen(String urlToChange) {
        return ViewModel.Current.getUrlByScreenSize(urlToChange);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

}
