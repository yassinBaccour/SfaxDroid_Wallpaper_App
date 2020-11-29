package com.sami.rippel.model.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by yassin baccour on 10/04/2017.
 */
@Root(name = "allahwaterrippleapplication", strict = false)
public class WallpapersRetrofitObject {

    @ElementList(name = "category", inline = true, required = false)
    @Path("ic_wallpaper")
    public List<WallpaperCategory> categoryList;
    @Element(name = "title")
    @Path("ic_wallpaper")
    private String applicationName;

    public WallpapersRetrofitObject() {
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<WallpaperCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<WallpaperCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
