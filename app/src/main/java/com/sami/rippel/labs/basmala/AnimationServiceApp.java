package com.sami.rippel.labs.basmala;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;

import java.io.File;
import java.io.IOException;

@TargetApi(19)
public class AnimationServiceApp {
    private static int incrementation = 0;
    public Context mContext;
    private ImageView mImageAtScreen;
    //private MediaPlayer mMediaPlayer;
    private boolean mRunning = true;
    private String mSoundPreferences;
    private String mPicturePreferences;
    private String mRandomPref;
    private int mVitessePref;
    private int mSizePref;
    private int mColor;
    private Bitmap mBitmapheader;
    private AlhpaImageAnim mTasking = null;

    public AnimationServiceApp(Context mContext) {
        this.mContext = mContext;
    }

    private void initPref() {
        mSoundPreferences = ViewModel.Current.sharedPrefsUtils.GetSetting("sound", "on");
        mPicturePreferences = ViewModel.Current.sharedPrefsUtils.GetSetting("eye", "on");
        mRandomPref = ViewModel.Current.sharedPrefsUtils.GetSetting("random", "off");
        mVitessePref = ViewModel.Current.sharedPrefsUtils.GetSetting("speed", 2);
        mSizePref = ViewModel.Current.sharedPrefsUtils.GetSetting("size", 3);
        mColor = ViewModel.Current.sharedPrefsUtils.GetSetting("color", 3);
    }

    private void initMusic() {
        /*
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.b);
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

         */
    }

    void startPreviewAnimation() {
        mImageAtScreen = new ImageView(mContext);
        initPref();
        mVitessePref = 3;
        initMusic();
        initAnimationObject();
        staranimation();
    }

    void startAnimation() {
        mImageAtScreen = new ImageView(mContext);
        initPref();
        initMusic();
        initAnimationObject();
        staranimation();
    }

    private void initAnimationObject() {
        int width = 400;
        int height = 400;

        if (mSizePref == 0) {
            width = ViewModel.Current.device.getScreenWidthPixels() / 4;
            height = ViewModel.Current.device.getScreenWidthPixels() / 4;
        } else if (mSizePref == 1) {
            width = ViewModel.Current.device.getScreenWidthPixels() / 2;
            height = ViewModel.Current.device.getScreenWidthPixels() / 2;
        } else if (mSizePref == 2) {
            width = ViewModel.Current.device.getScreenWidthPixels()
                    - ViewModel.Current.device.getScreenWidthPixels() / 4;
            height = ViewModel.Current.device.getScreenWidthPixels()
                    - ViewModel.Current.device.getScreenWidthPixels() / 4;
        } else if (mSizePref == 3) {
            width = ViewModel.Current.device.getScreenWidthPixels() - 20;
            height = ViewModel.Current.device.getScreenWidthPixels() - 20;
        }
        WindowManager.LayoutParams mLocalLayoutParams1 = new WindowManager.LayoutParams(2006);
        mLocalLayoutParams1.format = 1;
        mLocalLayoutParams1.width = width;
        mLocalLayoutParams1.height = height;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                width, height);
        mImageAtScreen.setLayoutParams(layoutParams);
        ViewModel.Current.device.mWindowManager.addView(mImageAtScreen,
                mLocalLayoutParams1);
        if (mRandomPref.equals("on")) {
            incrementation++;
            InitialiseIncrementationDrawable();
            if (incrementation == 20) {
                incrementation = 0;
            }
        } else {
            initialiseCurrentDrawable();
        }
    }

    private void initialiseCurrentDrawable() {
        try {
            File mBackgroundFile = new File(ViewModel.Current.sharedPrefsUtils
                    .GetSetting(Constants.KEY_BASMALA_PREFERENCES_PATH, ""));
            if (mBackgroundFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                mBitmapheader = ViewModel.Current.bitmapUtils
                        .changeImageColor(BitmapFactory.decodeFile(mBackgroundFile.getPath(),
                                options), mColor);
            }
        } catch (Exception ignored) {
        }
    }

    private void InitialiseIncrementationDrawable() {

    }

    private void staranimation() {
        if (mSoundPreferences.equals("on")) {
            //mMediaPlayer.start();
        }
        if (mPicturePreferences.equals("on")) {
            if (mTasking == null) {
                mTasking = new AlhpaImageAnim(mImageAtScreen, mBitmapheader, mVitessePref);
                mTasking.execute();
            }
        }
    }

    void stopAnimationIfRunning() {

        if (mTasking != null) {
            mTasking.cancel(true);
        }
        try {
            if (mImageAtScreen != null && mImageAtScreen.isAttachedToWindow()) {
                ViewModel.Current.device.mWindowManager
                        .removeView(mImageAtScreen);
                mImageAtScreen = null;
                if (!mBitmapheader.isRecycled())
                    mBitmapheader.recycle();
            }
        } catch (Exception ignored) {
        }
    }

    void stopPlayer() {
        /*
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            mMediaPlayer.stop();

         */
    }

    public class AlhpaImageAnim extends AsyncTask<Void, Integer, Void> {
        private int mFps = 80;
        private int mNbImage = 0;
        private ImageView mAtScreen;
        private Bitmap mDheader;
        private int mVitesse;

        public AlhpaImageAnim(ImageView mAtScreen, Bitmap mDheader, int mVitesse) {
            this.mAtScreen = mAtScreen;
            this.mDheader = mDheader;
            this.mVitesse = mVitesse;
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            mRunning = false;
            mTasking = null;
        }

        @Override
        protected void onPreExecute() {
            mAtScreen.setImageBitmap(mDheader);
        }


        @Override
        protected Void doInBackground(Void... params) {
            while (mRunning) {
                try {
                    Thread.sleep(mFps);
                    mNbImage++;
                    if (mVitesse == 0) {
                        if (mNbImage >= 500) {
                            mRunning = false;
                        }
                        publishProgress(mNbImage);
                    } else if (mVitesse == 1) {
                        if (mNbImage >= 50) {
                            mRunning = false;
                        }

                        publishProgress(mNbImage);

                    } else if (mVitesse == 2) {
                        if (mNbImage >= 25) {
                            mRunning = false;
                        }
                        publishProgress(mNbImage);

                    } else if (mVitesse == 3) {
                        if (mNbImage >= 12) {
                            mRunning = false;
                        }
                        publishProgress(mNbImage);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... frame) {
        }

        @Override
        protected void onPostExecute(Void result) {
            mTasking = null;
            ViewModel.Current.device.mWindowManager.removeView(mImageAtScreen);
            mAtScreen = null;
            if (mDheader != null && !mDheader.isRecycled()) {
                mDheader.recycle();
                mDheader = null;
            }
        }
    }
}
