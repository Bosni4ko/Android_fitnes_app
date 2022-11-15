package com.coursework.fitnessapp.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.coursework.fitnessapp.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class AddToWorkoutExerciseActivity extends AppCompatActivity {

    private ExpandableTextView expTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_workout_exercise);

        expTv = findViewById(R.id.expand_text_view);
    }
}