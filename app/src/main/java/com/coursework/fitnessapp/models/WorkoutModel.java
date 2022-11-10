package com.coursework.fitnessapp.models;

import java.time.Period;
import java.util.Date;
public class WorkoutModel {
    public static final String DEFAULT_STATUS = "default_status";

    private String id;
    private String name;
    private String description;
    private ExerciseModel[] exerciseModels;
    private Date dateTime;
    private Period length;
    private String status;
    private String type;

    public WorkoutModel(String id, String name,String description, ExerciseModel[] exerciseModels, Date dateTime,String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exerciseModels = exerciseModels;
        this.dateTime = dateTime;
        this.status = DEFAULT_STATUS;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public ExerciseModel[] getExerciseModels() {
        return exerciseModels;
    }

    public void setExerciseModels(ExerciseModel[] exerciseModels) {
        this.exerciseModels = exerciseModels;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Period getLength() {
        return length;
    }

    public void setLength(Period length) {
        this.length = length;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
