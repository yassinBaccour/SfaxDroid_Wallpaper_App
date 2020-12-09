package com.sfaxdroid.framecollage;

import java.io.File;

public class ApplicationHandler {

    private static ApplicationHandler handler;

    private ApplicationHandler() {
    }

    public static ApplicationHandler getInstance() {
        if (handler == null)
            handler = new ApplicationHandler();
        return handler;
    }

    File getOrCreateFolder(String folder, IMAGES imagePath) {

        String strImageFolder = folder + File.separator + imagePath.name();
        File imageFolder = new File(strImageFolder);
        if (!imageFolder.exists()) {
           boolean isCreated = imageFolder.mkdirs();
           if (!isCreated)
               return null;
        }
        return imageFolder;
    }

    public enum IMAGES {
        Cache,
        FrameImages
    }
}
