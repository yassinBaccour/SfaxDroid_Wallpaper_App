package com.sfaxdoird.anim.img;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;

public class Utils {

    public static File getTemporaryDouaDir(Context context, String folder, String appName) {
        File zipDestination = new File(getTemporaryDir(context, appName), folder);
        if (!zipDestination.exists()) {
            zipDestination.mkdirs();
        }
        return zipDestination;
    }

    private static File getTemporaryDir(Context context, String appName) {
        File temporaryDir = new File(context.getFilesDir(),
                appName + "/temp");
        if (!temporaryDir.exists()) {
            temporaryDir.mkdirs();
        }
        return temporaryDir;
    }



}
