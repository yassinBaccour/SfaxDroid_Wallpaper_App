package com.sami.rippel.livewallpapers.lwptimer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.ui.activity.GalleryWallpaperActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class WallpaperSchedulerActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTxtstatus;
    private TextView mTxtNotForget;
    private CoordinatorLayout mRootLayout;
    private Button mButtonActive;
    private Button mButtonClose;
    private RadioGroup mRadioSexGroup;
    private RadioButton mRadioOneHoure;
    private RadioButton mRadioSixHoure;
    private RadioButton mRadioDouzeHoure;
    private RadioButton mRadioOneDayHoure;
    private ProgressBar mProgressBar;
    private JobScheduler mScheduler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_scheduler);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
        mScheduler = (JobScheduler) getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mRadioOneHoure = (RadioButton) findViewById(R.id.radioOneHoure);
        mRadioSixHoure = (RadioButton) findViewById(R.id.radioSixHoure);
        mRadioDouzeHoure = (RadioButton) findViewById(R.id.radioDouzeHoure);
        mRadioOneDayHoure = (RadioButton) findViewById(R.id.radioOneDayHoure);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        Button mBtnAddWallpaper = (Button) findViewById(R.id.buttonAddLwp);
        Button mBtnListWallpaper = (Button) findViewById(R.id.buttonLWPList);
        mTxtstatus = (TextView) findViewById(R.id.txtstatus);
        mRadioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mButtonActive = (Button) findViewById(R.id.buttonActive);
        mButtonClose = (Button) findViewById(R.id.buttonClose);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTxtNotForget = (TextView) findViewById(R.id.txtNotForget);
        mTxtNotForget.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mRadioSexGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            List<JobInfo> mAllPendingJobs = mScheduler.getAllPendingJobs();
            if (mAllPendingJobs.size() > 0)
                removeJob();
        });

        mBtnAddWallpaper.setOnClickListener(view -> openAddWallpaperActivity());

        mBtnListWallpaper.setOnClickListener(view -> {
            Intent intent = new Intent(
                    WallpaperSchedulerActivity.this,
                    GalleryWallpaperActivity.class);
            intent.putExtra("LwpName", Constants.KEY_ADDED_LIST_TIMER_LWP);
            startActivity(intent);
        });

        mButtonActive.setOnClickListener(view -> {
            if (ViewModel.Current.fileUtils.getPermanentDirListFiles() != null) {
                int nbFile = ViewModel.Current.fileUtils.getPermanentDirListFiles().size();
                if (nbFile > 3) {
                    new ActiveServiceAndSetFirstWallpapersInBackground().execute("");
                    mButtonActive.setCompoundDrawablesWithIntrinsicBounds(null,
                            getResources().getDrawable(R.mipmap.ic_active_img_on),
                            null,
                            null);
                    mButtonActive.setTextColor(getResources().getColor(R.color.redflatui));
                    mButtonClose.setCompoundDrawablesWithIntrinsicBounds(null,
                            getResources().getDrawable(R.mipmap.ic_close_img),
                            null,
                            null);
                    mButtonClose.setTextColor(Color.WHITE);

                } else {
                    ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.nofiletimer));
                }

            }
        });

        mButtonClose.setOnClickListener(view -> {
            removeJob();
            mButtonClose.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_close_img_on),
                    null,
                    null);
            mButtonClose.setTextColor(getResources().getColor(R.color.redflatui));
            mButtonActive.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_active_img),
                    null,
                    null);
            mButtonActive.setTextColor(Color.WHITE);
        });

        List<JobInfo> allPendingJobs = mScheduler.getAllPendingJobs();
        if (allPendingJobs.size() > 0) {
            JobInfo jobInfo = allPendingJobs.get(0);
            setCheckedTime(jobInfo.getIntervalMillis());
            mTxtstatus.setText(getString(R.string.onswitch));
            mTxtNotForget.setVisibility(View.VISIBLE);
            mTxtstatus.setTextColor(getResources().getColor(R.color.green));
            mButtonActive.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_active_img_on),
                    null,
                    null);
            mButtonActive.setTextColor(getResources().getColor(R.color.redflatui));
        } else {
            mTxtstatus.setText(getString(R.string.offswitch));
            mTxtNotForget.setVisibility(View.GONE);
            mTxtstatus.setTextColor(getResources().getColor(R.color.red));
            setCheckedTime(0);
            mButtonClose.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_close_img_on),
                    null,
                    null);
            mButtonClose.setTextColor(getResources().getColor(R.color.redflatui));
        }
    }

    public void openAddWallpaperActivity() {
        Intent intent = new Intent(
                WallpaperSchedulerActivity.this,
                GalleryWallpaperActivity.class);
        intent.putExtra("LwpName", Constants.KEY_ADD_TIMER_LWP);
        startActivity(intent);
    }

    public void setCheckedTime(long time) {
        if (time == 3600000)
            mRadioOneHoure.setChecked(true);
        else if (time == 3600000 * 6)
            mRadioSixHoure.setChecked(true);
        else if (time == 3600000 * 12)
            mRadioDouzeHoure.setChecked(true);
        else if (time == 3600000 * 24)
            mRadioOneDayHoure.setChecked(true);
        else mRadioOneHoure.setChecked(true);
    }

    public long getSelectedTime(String selectedText) {
        if (selectedText.equals(getString(R.string.onehoure)))
            return 3600000;
        else if (selectedText.equals(getString(R.string.sixhoure)))
            return 3600000 * 6;
        else if (selectedText.equals(getString(R.string.douzeoure)))
            return 3600000 * 12;
        else if (selectedText.equals(getString(R.string.oneday)))
            return 3600000 * 24;
        else
            return 30000;
    }

    public void setWallpaperFromFile() {
        File myFile = ViewModel.Current.fileUtils.getPermanentDirListFiles().get(0);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        if (myFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(myFile.getPath(),
                    options);
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                if (bitmap != null) {
                    Bitmap background = Bitmap.createScaledBitmap(bitmap,
                            ViewModel.Current.device.getScreenWidthPixels(),
                            ViewModel.Current.device.getScreenHeightPixels(),
                            true);
                    if (background != null)
                        wallpaperManager.setBitmap(background);
                    bitmap.recycle();
                }
            } catch (IOException ignored) {
            }
        }
    }


    public boolean activeJob() {
        if (ViewModel.Current.fileUtils.getPermanentDirListFiles() != null) {
            int nbFile = ViewModel.Current.fileUtils.getPermanentDirListFiles().size();
            if (nbFile > 3) {
                int selectedId = mRadioSexGroup.getCheckedRadioButtonId();
                RadioButton mRadioSelectedButton = (RadioButton) findViewById(selectedId);
                String selectedText = mRadioSelectedButton.getText().toString();
                int mJobId = 1;
                JobInfo jobInfo = new JobInfo.Builder(
                        mJobId, new ComponentName(WallpaperSchedulerActivity.this, RetrieveWallpaperService.class))
                        .setPeriodic(getSelectedTime(selectedText))
                        .setPersisted(true)
                        .build();
                mScheduler.schedule(jobInfo);
                setWallpaperFromFile();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void showDialogNoMinFiles() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.addwalltitle))
                .setMessage(getString(R.string.addwalldesc))
                .setPositiveButton(getString(R.string.addwallok), (arg0, arg1) -> openAddWallpaperActivity())
                .setNegativeButton(getString(R.string.addwallcancel), (arg0, arg1) -> {
                    // Some stuff to do when cancel got clicked
                })
                .show();
    }

    public void removeJob() {
        mScheduler.cancelAll();
        mTxtstatus.setText(getString(R.string.offswitch));
        mTxtNotForget.setVisibility(View.GONE);
        mTxtstatus.setTextColor(getResources().getColor(R.color.red));
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(getString(R.string.TitleTimerLwp));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class ActiveServiceAndSetFirstWallpapersInBackground extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return activeJob();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressBar.setVisibility(View.GONE);
            try {
                if (result) {
                    mTxtstatus.setText(getString(R.string.onswitch));
                    mTxtNotForget.setVisibility(View.VISIBLE);
                    mTxtstatus.setTextColor(getResources().getColor(R.color.green));
                    ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.timeron));
                } else {
                    Activity activity = WallpaperSchedulerActivity.this;
                    if (activity != null) {
                        showDialogNoMinFiles();
                    }
                }

            } catch (Exception ignored) {
            }
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
