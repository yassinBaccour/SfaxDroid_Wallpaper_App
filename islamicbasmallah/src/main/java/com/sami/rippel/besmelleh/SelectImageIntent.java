package com.sami.rippel.besmelleh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SelectImageIntent extends Activity {

	public final String TAG = "CamIntent";
	public String filename;
	public final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	public final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 2;
	String[] fileList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setView();
	}

	public void setView() {
		
		
				
		
		try {
			filename ="img.jpg";
			Log.i(TAG,"File Name : "+filename);			
			
			
			 Intent intent = new Intent();
             intent.setType("image/*");
             intent.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(Intent.createChooser(intent,
                     "Select Picture"), SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
				 
		}catch(Exception ex)
		{
			Log.e(TAG,"Error : "+ex.getMessage());
		}
			
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e(TAG,"activity return : "+requestCode);
		switch (requestCode) {
		case SELECT_IMAGE_ACTIVITY_REQUEST_CODE:

			if (resultCode == RESULT_OK) {
				// use imageUri here to access the image
				
				Log.i(TAG, "Result Ok ");
				
				Uri selectedImageUri = data.getData();
	           
				doCrop(selectedImageUri);
			} else if (resultCode == RESULT_CANCELED) {
				
				Log.i(TAG, "Result RESULT_CANCELED ");
				finish();
			} else {
				Log.i(TAG, "Result Unknown");
				finish();
			}
			break;

		case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode != RESULT_OK)
				return;
			try {
				Log.e(TAG,"Crop activity return");
				
				Bundle extras = data.getExtras();
				if (extras != null) {
					String mPath = Environment.getExternalStorageDirectory().getAbsolutePath()
							+ "/.PicRipple/"+filename;	
					Bitmap photo = BitmapFactory.decodeFile(mPath);
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

					File f = new File(filename);
					Log.e(TAG, "Cropped file saved at : " + f.getAbsolutePath());
					// write the bytes in file
					FileOutputStream fo = openFileOutput(filename, MODE_WORLD_READABLE);
					fo.write(bytes.toByteArray());
					fo.flush();
					fo.close();
					
					
					
					finish();

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.i(TAG,"Error : "+e.getMessage());
				finish();
			}
			break;
		}
	}

	private void doCrop(Uri selectedImageUri) {
		Log.i(TAG, "Enter in DoCrop ");
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		Log.i(TAG, "Intent Set");
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);
		
		int size = list.size();
		Log.i(TAG, "Resolve Info Size : "+size);
		if (size == 0) {
			Toast.makeText(this, "Can not find image crop app",
					Toast.LENGTH_SHORT).show();

			return;
		} else {
			try {
		
			Log.i(TAG, "Crop File Uri : "+selectedImageUri);
			intent.setData(selectedImageUri);
			
	//		int s = pxToDP(100);
			intent.putExtra("outputX", 640);
			intent.putExtra("outputY", 960);
			intent.putExtra("aspectX", 2);
			intent.putExtra("aspectY", 3);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", false);
//			intent.putExtra("circleCrop", "true");
			String mPath = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/.PicRipple";		
			
			//try {
				File f = new File(mPath);
				
				if (!f.exists())
					f.mkdirs();
				f=new File(mPath+"/"+filename);
				if (f.exists())
					f.delete();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			Intent i = new Intent(intent);
			ResolveInfo res = list.get(0);

			i.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			Log.i(TAG, "Cropping About to Start");
			startActivityForResult(i, CROP_IMAGE_ACTIVITY_REQUEST_CODE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
