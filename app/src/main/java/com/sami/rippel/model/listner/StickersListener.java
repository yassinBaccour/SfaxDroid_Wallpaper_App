package com.sami.rippel.model.listner;

/**
 * Created by yassin baccour on 16/05/2016.
 */
public interface StickersListener {

    void hideProgressLoader();

    void downloadAndPutImageAtScreen(String url);

    void downloadAndPutFrameAtScreen(String url, String resizeType);

    void addTextFromFragment();

    void addTextToView(String text);

    void changeTextFont();

    void resetAllView();

    void changeTextSize();

    void changeTextBackground();

    void morePadding();

    void lestPadding();

    void RemoveFrame();
}
