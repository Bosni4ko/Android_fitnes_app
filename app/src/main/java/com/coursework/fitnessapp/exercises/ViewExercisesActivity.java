package com.coursework.fitnessapp.exercises;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.ExerciseModel;

import java.util.ArrayList;

public class ViewExercisesActivity extends AppCompatActivity {
    ArrayList<ExerciseModel> exercises;
    private String type;
    private String action;
    private TextView noExercisesText;
    private RecyclerView exercisesRecView;
    private Button backBtn;

    private DataBaseHelper dataBaseHelper;
    private ExercisesRecViewAdapter adapter;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercises);

        Intent intent = getIntent();
        type = intent.getExtras().get("type").toString();
        action = intent.getExtras().get("action").toString();
        dataBaseHelper = new DataBaseHelper(this);
        exercises = dataBaseHelper.getAllExercisesOfType(type);

        noExercisesText = findViewById(R.id.noExercisesText);
        if(!exercises.isEmpty()){
            exercisesRecView = findViewById(R.id.exercisesRecycleView);
            adapter = new ExercisesRecViewAdapter(action);
            adapter.setExercises(exercises);
            exercisesRecView.setAdapter(adapter);
            exercisesRecView.setLayoutManager(new LinearLayoutManager(this));
        }else {
            noExercisesText.setVisibility(View.VISIBLE);
        }
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        exercises = dataBaseHelper.getAllExercisesOfType(type);
        if(!exercises.isEmpty()){
            adapter.setExercises(exercises);
            noExercisesText.setVisibility(View.GONE);
        }else {
            noExercisesText.setVisibility(View.VISIBLE);
        }
    }
}