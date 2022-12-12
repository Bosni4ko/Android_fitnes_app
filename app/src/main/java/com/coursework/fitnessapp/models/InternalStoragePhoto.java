package com.coursework.fitnessapp.models;

import android.graphics.Bitmap;

public class InternalStoragePhoto {
    private String name;
    private Bitmap bmp;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }
}
