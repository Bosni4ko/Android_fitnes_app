package com.coursework.fitnessapp.exercises;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.ExerciseModel;

import java.util.ArrayList;

public class DefaultExerciseActivity extends AppCompatActivity {

    ArrayList<ExerciseModel> exercises;
    private RecyclerView exercisesRecView;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_exercise);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        exercises = dataBaseHelper.getAllExercisesOfType("Default");

        exercisesRecView = findViewById(R.id.exercisesRecycleView);
        ExercisesRecViewAdapter adapter = new ExercisesRecViewAdapter();
        adapter.setExercises(exercises);

        exercisesRecView.setAdapter(adapter);
        exercisesRecView.setLayoutManager(new LinearLayoutManager(this));
    }
}