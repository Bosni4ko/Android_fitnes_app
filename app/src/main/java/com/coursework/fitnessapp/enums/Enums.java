package com.coursework.fitnessapp.enums;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Enums {
    public enum WorkoutStatus{
        FINISHED,
        SKIPPED,
        WAITING
    }
    public enum WorkoutType{
        DEFAULT,
        CUSTOM
    }
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM uuuu");
}
