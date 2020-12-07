package com.sami.rippel.model;

import android.graphics.Bitmap;
import android.os.Environment;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String SFAXDROID_LINK = "market://search?q=pub:SFAXDROID";
     public static final String DETAIL_IMAGE_POS = "pos";
    public static final String LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER = "listFileToSendToDetailViewPager";
    public static final String APP_PACKAGE = "market://details?id=com.sami.rippel.allah";
    public static final String BASE_URL = "http://androidsporttv.com/islamicimages/RetorFitFormat/";
    public static final String GT_FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FrameImages/";
    public static final String RATING_MESSAGE = "ratingmessage";
    public static final String RATING_NON = "Non";
    public static final String RATING_YES = "Oui";
    public static final int MIN_WIDHT = 600;
    public static final int MIN_HEIGHT = 1000;
    public static final String PNG_BASMALA_STICKERS_FILE_NAME = "basmala.zip";
    public static final String KEY_LWP_NAME = "LwpName";
    public static final String KEY_DOUA_LWP = "DouaLWP";
    public static final String KEY_RIPPLE_LWP = "RippleLwp";
    public static final String KEY_BASMALA_STIKERS = "BasmalaStikers";
    public static final String KEY_NAME_OF_ALLAH_2_D = "NameOfAllah2DLWP";
    public static final String KEY_TEXTURE = "keyTexture";
    //FIXME unmodifiableSet of NAME_OF_ALLAH_TAB

    public static final String KEY_DEFAULT = "STICKERS";
    public static final String KEY_TEXT = "TEXT";
    public static final String KEY_HELP = "HELP";
    public static final String SQUARE_TYPE = "square";
    public static final String LANDSCAPE_TYPE = "landscape";
    public static final String CATEGORY_STIKERS = "Stikers";
    public static final String CATEGORY_FLOWER = "flower";
    public static final String PREVIEW_JPG = "_preview.jpg";
    public static final String PNG_FORMAT = ".png";
    public static Bitmap currentBitmaps;
    public static String FilePath = "";
    public static final String URL_BIG_WALLPAPER_FOLDER = "/islamicimages/";
    public static final String URL_SMALL_WALLPAPER_FOLDER = "/islamicimagesmini/";
    public static final String FB_PACKAGE = "com.facebook.katana";
    public static final String INSTAGRAM_PACKAGE = "com.instagram.android";
    public static final String SNAP_PACKAGE = "com.instagram.android";
}
