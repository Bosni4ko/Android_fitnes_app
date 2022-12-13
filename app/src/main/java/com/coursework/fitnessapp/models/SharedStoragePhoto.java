package com.coursework.fitnessapp.models;

import android.net.Uri;

public class SharedStoragePhoto {
    private Long id;
    private String name;
    private Integer width;
    private Integer height;
    private Uri contentUri;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Uri getContentUri() {
        return contentUri;
    }

    public void setContentUri(Uri contentUri) {
        this.contentUri = contentUri;
    }

}
