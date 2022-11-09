package com.coursework.fitnessapp.models;


import java.time.Period;

public class ExerciseModel {
    private Integer id;
    private String name;
    private String description;
    private String[] imageUrls;
    private String videoUrl;
    private Period length;
    private Integer defaultCount;
    private Integer count;

    public ExerciseModel(Integer id, String name, String description, String[] imageUrl, String videoUrl, Period length, Integer defaultCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrl == null ? null : imageUrl;
        this.videoUrl = videoUrl == null ? null : videoUrl;
        this.length = length;
        this.defaultCount = defaultCount;
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

    public String[] getImageUrl() {
        return imageUrls;
    }

    public void setImageUrl(String[] imageUrl) {
        this.imageUrls = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Period getLength() {
        return length;
    }

    public void setLength(Period length) {
        this.length = length;
    }

    public Integer getDefaultCount() {
        return defaultCount;
    }

    public void setDefaultCount(Integer defaultCount) {
        this.defaultCount = defaultCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
