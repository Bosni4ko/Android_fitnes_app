package com.coursework.fitnessapp.models;

import android.net.Uri;

import java.util.ArrayList;

public class Image {
    private Uri image;
    private String name;

    public Image(Uri image, String name) {
        this.image = image;
        this.name = name;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
