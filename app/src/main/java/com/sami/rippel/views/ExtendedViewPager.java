package com.sami.rippel.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class ExtendedViewPager extends ViewPager {
	public ExtendedViewPager(Context mContext) {
		super(mContext);
	}

	public ExtendedViewPager(Context mContext, AttributeSet mAttrs) {
		super(mContext, mAttrs);
	}

	@Override
	protected boolean canScroll(View mView, boolean checkV, int dx, int x, int y) {
		if (mView instanceof TouchImageView) {
			return ((TouchImageView) mView).canScrollHorizontallyFroyo(-dx);
		} else {
			return super.canScroll(mView, checkV, dx, x, y);
		}
	}
}
