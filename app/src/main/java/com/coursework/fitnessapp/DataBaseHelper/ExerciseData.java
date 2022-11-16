package com.coursework.fitnessapp.DataBaseHelper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;

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
                new TimeDuration("00:00:20"),
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
                new TimeDuration("00:01:00"),
                15,
                "Default"
        ));
    }
}
