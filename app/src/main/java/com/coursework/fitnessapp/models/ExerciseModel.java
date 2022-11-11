package com.coursework.fitnessapp.models;


import java.time.Duration;
import java.time.Period;

public class ExerciseModel {
    private Integer id;
    private String name;
    private String description;
    private String[] imageUrls;
    private String videoUrl;
    private Duration defaultLength;
    private Duration length;
    private Integer defaultCount;
    private Integer count;
    private String type;

    public ExerciseModel(Integer id, String name, String description, String[] imageUrl, String videoUrl, Duration defaultlength, Integer defaultCount,String type) {
        this.id = id == null ? null : id;
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrl == null ? null : imageUrl;
        this.videoUrl = videoUrl == null ? null : videoUrl;
        this.defaultLength = defaultlength;
        this.defaultCount = defaultCount;
        this.type = type;
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

    public Duration getLength() {
        return length;
    }

    public void setLength(Duration length) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Duration getDefaultLength() {
        return defaultLength;
    }

    public void setDefaultLength(Duration defaultLength) {
        this.defaultLength = defaultLength;
    }
}
