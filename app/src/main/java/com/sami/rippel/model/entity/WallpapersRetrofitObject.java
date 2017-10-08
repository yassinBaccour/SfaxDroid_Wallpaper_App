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

    @Element(name = "title")
    @Path("wallpapers")
    private String applicationName;

    @ElementList(name = "category", inline = true, required = false)
    @Path("wallpapers")
    public List<WallpaperCategory> categoryList;

    public WallpapersRetrofitObject() {
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setCategoryList(List<WallpaperCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public List<WallpaperCategory> getCategoryList() {
        return categoryList;
    }
}
