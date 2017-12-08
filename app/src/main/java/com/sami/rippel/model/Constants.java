package com.sami.rippel.model;

import android.graphics.Bitmap;
import android.os.Environment;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String CUSTOM_LWP = "custom";
    public static final String START_APP_KEY = "211624686";
    public static final String SFAXDROID_LINK = "market://search?q=pub:SFAXDROID";
    public static final String RIPPLE_SIZE_KEY = "ripple_size";
    public static final String RIPPLE_SPEED_KEY = "ripple_speed";
    public static final String SOUND_KEY = "sound_on";
    public static final String CHANGE_IMAGE_KEY = "Select_Image";
    public static final String PREFERENCESNAME = "AllahWaterRippleLwp";
    public static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    public static final String DETAIL_IMAGE_POS = "pos";
    public static final String LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER = "listFileToSendToDetailViewPager";
    public static final String APP_PACKAGE = "market://details?id=com.sami.rippel.allah";
    //XML
    public static final String BASE_URL = "http://androidsporttv.com/islamicimages/RetorFitFormat/";

    //Zip
    public static final String URL_DOUA_PNG = "http://androidsporttv.com/yassin123441/doua.zip";
    public static final String URL_BASMALA_PNG = "http://androidsporttv.com/yassin123441/basmala.zip";

    //Labs
    public static final String GT_DOUA_PNG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DouaPng/";
    public static final String GT_FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FrameImages/";
    public static final String RATING_MESSAGE = "ratingmessage";
    public static final String RATING_NON = "Non";
    public static final String RATING_YES = "Oui";
    public static final int MIN_WIDHT = 600;
    public static final int MIN_HEIGHT = 1000;
    public static final String PNG_ZIP_FILE_NAME = "doua.zip";
    public static final String DOUA_PNG_BACKFROUND_FILE_NAME = "doua.jpg";
    public static final String PNG_ZIP_DOUA_EXTRACTED_FOLDER = "DouaFolder";
    public static final String PNG_BASMALA_STICKERS_FILE_NAME = "basmala.zip";
    public static final String URL_TO_DOWNLOAD = "URLTODOWNLOAD";
    public static final String KEY_LWP_NAME = "LwpName";
    public static final String KEY_DOUA_LWP = "DouaLWP";
    public static final String KEY_ADD_TIMER_LWP = "TimerLWPadd";
    public static final String KEY_ADDED_LIST_TIMER_LWP = "TimerLWPList";
    public static final String KEY_RIPPLE_LWP = "RippleLwp";
    public static final String KEY_BASMALA_STIKERS = "BasmalaStikers";
    public static final String KEY_BASMALA_FOLDER_CONTAINER = "BasmalaFolder";
    public static final String KEY_NAME_OF_ALLAH_2_D = "NameOfAllah2DLWP";
    public static final String KEY_BASMALA_PREFERENCES_PATH = "KeySettingBasmalaImgPath";
    public static final String KEY_TEXTURE = "keyTexture";
    //FIXME unmodifiableSet of NAME_OF_ALLAH_TAB
    public static final List<String> NAME_OF_ALLAH_TAB = Arrays.asList("الرحمن", "الرحيم", "الملك", "القدوس", "السلام", "المؤمن"
            , "المهيمن", "العزيز", "الجبار", "المتكبر", "الخالق", "البارئ", "المصور",
            "الغفار", "القهار", "الوهاب", "الرزاق", "الفتاح",
            "العليم", "القابض", "الباسط", "الخافض", "الرافع",
            "المعز", "المذل", "السميع", "البصير", "الحكم",
            "العدل", "اللطيف", "الخبير", "الحليم", "العظيم",
            "الغفور", "الشكور", "العلى", "الكبير", "الحفيظ",
            "المقيت", "الحسيب", "الجليل", "الكريم", "الرقيب",
            "المجيب", "الواسع", "الحكيم", "الودود", "المجيد",
            "الباعث", "الشهيد", "الحق", "الوكيل", "القوى",
            "المتين", "الولى", "الحميد", "المحصى", "المبدئ", "المعيد", "المحيى", "المميت", "الحي", "القيوم",
            "الواجد", "الماجد", "الواحد", "الاحد", "الصمد", "القادر", "المقتدر",
            "المقدم", "المؤخر", "الأول", "الأخر", "الظاهر", "الباطن", "الوالي",
            "المتعالي", "البر", "التواب", "المنتقم", "العفو", "الرؤوف",
            "مالك الملك", "ذو الجلال و الإكرام", "المقسط", "الجامع", "الغنى", "المغنى",
            "المانع", "الضار", "النافع", "النور", "الهادي", "البديع",
            "الباقي", "الوارث", "الرشيد", "الصبور"
    );
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
    public static Boolean ifBackground_changed = false;
    public static int nb_incrementation_after_change = 0;

    public static final String URL_BIG_WALLPAPER_FOLDER = "/islamicimages/";
    public static final String URL_SMALL_WALLPAPER_FOLDER = "/islamicimagesmini/";

    public static final String FB_PACKAGE = "com.facebook.katana";
    public static final String INSTAGRAM_PACKAGE = "com.instagram.android";
    public static final String SNAP_PACKAGE = "com.instagram.android";
}
