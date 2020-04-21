package com.sami.rippel.feature.singleview;

public class ItemWrapperList<T> {

    private T object;
    private int type;

    public ItemWrapperList(T object, int type) {
        this.object = object;
        this.type = type;
    }

    public int getItemType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getObject() {
        return object;
    }
}
