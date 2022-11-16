package com.coursework.fitnessapp.models;


import com.coursework.fitnessapp.supportclasses.TimeDuration;

import java.time.Duration;

public class ExerciseModel {
    private Integer id;
    private String name;
    private String description;
    private String previewUrl;
    private String[] imageUrls;
    private String videoUrl;
    private TimeDuration defaultLength;
    private TimeDuration length;
    private int defaultCount;
    private int count;
    private String type;

    public ExerciseModel(Integer id, String name, String description,String previewUrl, String[] imageUrl, String videoUrl, TimeDuration defaultLength, int defaultCount,String type) {
        this.id = id == null ? null : id;
        this.name = name;
        this.description = description;
        this.previewUrl = previewUrl == null ? null : previewUrl;
        this.imageUrls = imageUrl == null ? null : imageUrl;
        this.videoUrl = videoUrl == null ? null : videoUrl;
        this.defaultLength = defaultLength;
        this.defaultCount = defaultCount;
        this.type = type;
    }
    public ExerciseModel(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public TimeDuration getLength() {
        return length;
    }

    public void setLength(TimeDuration length) {
        this.length = length;
    }

    public int getDefaultCount() {
        return defaultCount;
    }

    public void setDefaultCount(Integer defaultCount) {
        this.defaultCount = defaultCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TimeDuration getDefaultLength() {
        return defaultLength;
    }

    public void setDefaultLength(TimeDuration defaultLength) {
        this.defaultLength = defaultLength;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}
