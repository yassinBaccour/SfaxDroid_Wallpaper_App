package com.sami.rippel.model.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by yassin baccour on 12/04/2017.
 */
@Root(name = "category", strict = false)
public class WallpaperCategory {

    @Element(name = "title", required = true)
    public String title;

    public String getTitle() {
        return title;
    }

    public List<WallpaperObject> getGetWallpapersList() {
        return getWallpapersList;
    }

    @ElementList(name="wallpaper", inline=true, required = false)
    public List<WallpaperObject> getWallpapersList;


}
