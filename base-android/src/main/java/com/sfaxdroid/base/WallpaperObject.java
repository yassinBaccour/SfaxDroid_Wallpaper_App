package com.sfaxdroid.base;

import android.os.Parcel;
import android.os.Parcelable;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "wallpaper", strict = false)
public class WallpaperObject implements Parcelable, Serializable {

    public WallpaperObject(String name, String desc, int resourceColor, int resourceUri) {
        this.name = name;
        this.desc = desc;
        this.resourcecolor = resourceColor;
        this.resourceUri = resourceUri;
    }

    public WallpaperObject(String name, String desc, int resourceColor, int resourceUri, LiveWallpaper lv) {
        this.name = name;
        this.desc = desc;
        this.resourcecolor = resourceColor;
        this.resourceUri = resourceUri;
        liveWallpaper = lv;
    }

    public LiveWallpaper getLiveWallpaper() {
        return liveWallpaper;
    }

    public int getResourceUri() {
        return resourceUri;
    }

    public int getResourcecolor() {
        return resourcecolor;
    }

    private int resourceUri;
    private int resourcecolor;

    @Element(name = "url", required = true)
    private String url;
    @Element(name = "name", required = false)
    private String name;
    @Element(name = "color", required = false)
    private String color;
    @Element(name = "desc", required = false)
    private String desc;
    @ElementList(name = "wallpaper", inline = true, required = false)
    @Path("subcategory")
    private List<WallpaperObject> subWallpapersCategoryList;

    LiveWallpaper liveWallpaper;

    protected WallpaperObject(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public WallpaperObject() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url.replace("_preview", "");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHeight() {
        return 1;
    }

    public String getPreviewUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WallpaperObject> getSubWallpapersCategoryList() {
        return subWallpapersCategoryList;
    }

    public void setSubWallpapersCategoryList(List<WallpaperObject> subWallpapersCategoryList) {
        this.subWallpapersCategoryList = subWallpapersCategoryList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }

    public static final Creator<WallpaperObject> CREATOR = new Creator<WallpaperObject>() {
        @Override
        public WallpaperObject createFromParcel(Parcel in) {
            return new WallpaperObject(in);
        }

        @Override
        public WallpaperObject[] newArray(int size) {
            return new WallpaperObject[size];
        }
    };
}
