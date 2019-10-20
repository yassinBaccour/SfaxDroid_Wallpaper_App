package com.sfaxman.islamwallpaper;

import com.sfaxman.islamwallpaper.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


public class ImageAdapter extends BaseAdapter {
	/** The parent context */
	private Context myContext;
	// Put some images to project-folder: /res/drawable/
	// format: jpg, gif, png, bmp, ...
	private int[] myImageIds = { R.drawable.islam1_00000, R.drawable.islam2_00000,R.drawable.islam3_00000, R.drawable.islam4_00000,R.drawable.islam5_00000, R.drawable.islam6_00000 };

	/** Simple Constructor saving the 'parent' context. */
	public ImageAdapter(Context c) {
		this.myContext = c;
	}

	// inherited abstract methods - must be implemented
	// Returns count of images, and individual IDs
	public int getCount() {
		return this.myImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	// Returns a new ImageView to be displayed,
	public View getView(int position, View convertView, 
		ViewGroup parent) {
		// Get a View to display image data 					
		ImageView iv = new ImageView(this.myContext);
		iv.setImageResource(this.myImageIds[position]);
		// Image should be scaled somehow
		//iv.setScaleType(ImageView.ScaleType.CENTER);
		//iv.setScaleType(ImageView.ScaleType.CENTER_CROP);			
		//iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		//iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		//iv.setScaleType(ImageView.ScaleType.FIT_END);
		// Set the Width & Height of the individual images
		iv.setLayoutParams(new Gallery.LayoutParams(150,250));
		return iv;
		
		
	}
}// ImageAdapter
