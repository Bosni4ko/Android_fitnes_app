package com.coursework.fitnessapp.models;

import java.time.Period;
import java.util.Date;
public class WorkoutModel {
    public static final String DEFAULT_STATUS = "default_status";

    private String id;
    private String name;
    private ExerciseModel[] exerciseModels;
    private Date dateTime;
    private Period length;
    private String status;

    public WorkoutModel(String id, String name, ExerciseModel[] exerciseModels, Date dateTime) {
        this.id = id;
        this.name = name;
        this.exerciseModels = exerciseModels;
        this.dateTime = dateTime;
        this.status = DEFAULT_STATUS;
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
}
