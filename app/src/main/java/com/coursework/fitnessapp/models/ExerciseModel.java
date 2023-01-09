package com.coursework.fitnessapp.models;


import com.coursework.fitnessapp.supportclasses.TimeDuration;

import java.util.ArrayList;

//#Model to store exercise
public class ExerciseModel {
    private Integer id;
    private String name;
    private String description;
    private String previewImageName;
    private ArrayList<String> imageNames;
    private TimeDuration defaultLength;
    private TimeDuration length;
    private int defaultCount;
    private int count;
    private String type;

    public ExerciseModel(Integer id, String name, String description, String previewImageName, ArrayList<String> imageNames, TimeDuration defaultLength, int defaultCount, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.previewImageName = previewImageName;
        this.imageNames = imageNames;
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

    public ArrayList<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(ArrayList<String> imageNames) {
        this.imageNames = imageNames;
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

    public String getPreviewImageName() {
        return previewImageName;
    }

    public void setPreviewImageName(String previewImageName) {
        this.previewImageName = previewImageName;
    }
}
