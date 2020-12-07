package com.sfaxdroid.timer;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;


import com.sfaxdroid.base.FileUtils;
import com.sfaxdroid.base.Utils;

import java.io.File;
import java.io.IOException;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RetrieveWallpaperService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        new RetrieveWallpaperAsync(this).execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void updateSchedulerSettings(int currentWallpaper) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("currentWallpaper", currentWallpaper);
        editor.apply();
    }

    private class RetrieveWallpaperAsync extends AsyncTask<JobParameters, Void, JobParameters> {
        private final JobService jobService;

        public RetrieveWallpaperAsync(JobService jobService) {
            this.jobService = jobService;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            Log.d("WallpaperService", "Wallpaper changed");
            int nbFile = FileUtils.Companion.getPermanentDirListFiles(getBaseContext(), getBaseContext().getString(R.string.app_namenospace)).size();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int currentWallpaper = sharedPref.getInt("currentWallpaper", 0);
            if (nbFile > 0) {
                if (currentWallpaper >= nbFile) {
                    updateSchedulerSettings(0);
                    currentWallpaper = 0;
                }
                File MyFile = FileUtils.Companion.getPermanentDirListFiles(getBaseContext(), getBaseContext().getString(R.string.app_namenospace)).get(currentWallpaper);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                if (MyFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(MyFile.getPath(),
                            options);
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                    try {
                        if (bitmap != null) {
                            Bitmap background = Bitmap.createScaledBitmap(bitmap, Utils.Companion.getScreenWidthPixels(getBaseContext()), Utils.Companion.getScreenHeightPixels(getBaseContext()), true);
                            if (background != null)
                                wallpaperManager.setBitmap(background);
                            bitmap.recycle();
                        }
                    } catch (IOException ignored) {
                    }
                }
                currentWallpaper = currentWallpaper + 1;
                updateSchedulerSettings(currentWallpaper);
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters params) {
            jobService.jobFinished(params, false);
        }
    }
}
