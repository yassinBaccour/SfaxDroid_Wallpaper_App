package com.sami.rippel.base;

import android.support.v4.view.PagerAdapter;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePagerAdapter<T> extends PagerAdapter {
	private final List<T> mItems;

	public BasePagerAdapter(ArrayList<T> data) {
		mItems = data;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	public T getItem(int position) {
		return mItems.get(position);
	}
}
