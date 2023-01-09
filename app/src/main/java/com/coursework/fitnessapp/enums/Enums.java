package com.coursework.fitnessapp.enums;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Enums {
    public enum WorkoutStatus{
        FINISHED,
        SKIPPED,
        WAITING,
        STOPPED,
    }
    public enum WorkoutType{
        CUSTOM
    }
    public enum ExerciseType{
        Default,
        Custom
    }
    public enum ExerciseAction{
        View,
        Add
    }
    public enum WorkoutAction{
        View,
        Start
    }
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM uuuu");
    public static DateTimeFormatter fromDbFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    public static int taskBreakCounter = 5;
}
