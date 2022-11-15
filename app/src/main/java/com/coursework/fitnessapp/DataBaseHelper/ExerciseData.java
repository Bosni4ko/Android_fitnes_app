package com.coursework.fitnessapp.DataBaseHelper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.coursework.fitnessapp.models.ExerciseModel;

import java.time.Duration;
import java.util.ArrayList;

public class ExerciseData
{
   ArrayList<ExerciseModel> exercises= new ArrayList();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ExerciseData() {
        exercises.add(new ExerciseModel(
                null,
                "TestName",
                "Long description.Long description.Long description.Long description.Long description.Long description.Long descriptionLong description",
                null,
                null,
                null,
                Duration.ofSeconds(30),
                10,
                "Default"
        ));
        exercises.add(new ExerciseModel(
                null,
                "TestName 2",
                "Long description 2.Long description 2.Long description.Long description.Long description.Long description.Long descriptionLong description",
                null,
                null,
                null,
                Duration.ofSeconds(60),
                15,
                "Default"
        ));
    }
}
