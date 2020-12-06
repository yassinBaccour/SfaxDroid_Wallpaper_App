package com.sami.rippel.model.listner;

/**
 * Created by yassin baccour on 16/05/2016.
 */
public interface AdsListener {
    void onTrackAction(String category, String name);

    void onOpenScreenTracker(String screenName);
}
