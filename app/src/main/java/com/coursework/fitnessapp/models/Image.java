package com.coursework.fitnessapp.models;

import android.net.Uri;

//#Model to store imported image Uri and name
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
