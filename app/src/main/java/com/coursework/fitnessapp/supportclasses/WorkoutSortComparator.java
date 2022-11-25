package com.coursework.fitnessapp.supportclasses;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.coursework.fitnessapp.models.WorkoutModel;

import java.util.Comparator;

public class WorkoutSortComparator implements Comparator<WorkoutModel> {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compare(WorkoutModel workout1, WorkoutModel workout2) {
        if(workout1.getDate().compareTo(workout2.getDate()) != 0){
            return (workout1.getDate().compareTo(workout2.getDate()));
        }
        else return workout1.getTime().compareTo(workout2.getTime());
    }
}
