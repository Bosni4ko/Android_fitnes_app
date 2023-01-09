package com.coursework.fitnessapp.models;

import com.coursework.fitnessapp.supportclasses.TimeDuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
//#Model to store workout data
public class WorkoutModel {

    private String id;
    private String name;
    private String description;
    private ArrayList<ExerciseModel> exerciseModels;
    private LocalDate date;
    private LocalTime time;
    private TimeDuration length;
    private String status;
    private String type;

    public WorkoutModel() {
    }

    public WorkoutModel(String id, String name, String description, ArrayList<ExerciseModel> exerciseModels, LocalDate date, LocalTime time, String type, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exerciseModels = exerciseModels;
        this.date = date;
        this.time = time;
        this.type = type;
        this.status = status;
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

    public ArrayList<ExerciseModel> getExerciseModels() {
        return exerciseModels;
    }

    public void setExerciseModels(ArrayList<ExerciseModel> exerciseModels) {
        this.exerciseModels = exerciseModels;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public TimeDuration getLength() {
        return length;
    }

    public void setLength(TimeDuration length) {
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
