package com.coursework.fitnessapp.models;

public class SavedWorkoutProgressModel {
    private Integer exerciseIndex;
    private Integer exTimer;
    private Integer wrkTimer;
    private String workoutId;

    public SavedWorkoutProgressModel() {
    }

    public SavedWorkoutProgressModel(Integer exerciseIndex, Integer exTimer, Integer wrkTimer, String workoutId) {
        this.exerciseIndex = exerciseIndex;
        this.exTimer = exTimer;
        this.wrkTimer = wrkTimer;
        this.workoutId = workoutId;
    }

    public Integer getExerciseIndex() {
        return exerciseIndex;
    }

    public void setExerciseIndex(Integer exerciseIndex) {
        this.exerciseIndex = exerciseIndex;
    }

    public Integer getExTimer() {
        return exTimer;
    }

    public void setExTimer(Integer exTimer) {
        this.exTimer = exTimer;
    }

    public Integer getWrkTimer() {
        return wrkTimer;
    }

    public void setWrkTimer(Integer wrkTimer) {
        this.wrkTimer = wrkTimer;
    }

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }
}
