package com.sami.rippel.model.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Created by yassin baccour on 13/04/2017.
 */
@Root(name = "app", strict=false)
public class UpdateApp {
    @Element(name="update")
    private String isUpdateNeeded = "";

    public String getIsUpdateNeeded() {
        return isUpdateNeeded;
    }

    public boolean isUpdateAppNeeded() {
        if (getIsUpdateNeeded().equals("true"))
            return true;
            else
                return false;
    }

}
