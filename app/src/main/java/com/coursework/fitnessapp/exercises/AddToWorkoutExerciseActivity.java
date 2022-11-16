package com.coursework.fitnessapp.exercises;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.time.Duration;

public class AddToWorkoutExerciseActivity extends AppCompatActivity {

    private TextView exerciseName;
    private EditText exerciseLength;
    private EditText exerciseCount;
    private TextView expandableDescription;
    private ImageView exercisePreviewImg;
    private ExpandableTextView expTv;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(AddToWorkoutExerciseActivity.this);
        ExerciseModel exercise;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_workout_exercise);
        initLayout();
        Intent intent = getIntent();
        int id = Integer.parseInt(intent.getStringExtra("exercise"));

        exercise = dataBaseHelper.getExerciseById(id);
        setExerciseValues(exercise);
    }

    private void initLayout(){
        exerciseName = findViewById(R.id.exerciseName);
        exerciseLength = findViewById(R.id.exerciseLength);
        exerciseCount = findViewById(R.id.exerciseCount);
        expandableDescription = findViewById(R.id.expandable_text);
        exercisePreviewImg = findViewById(R.id.exercisePreviewImg);
        expTv = findViewById(R.id.expand_description);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setExerciseValues(ExerciseModel exercise){
        exerciseName.setText(exercise.getName());
        exerciseLength.setText(exercise.getDefaultLength().getToStringDuration());
        exerciseCount.setText(String.valueOf(exercise.getDefaultCount()));
        expandableDescription.setText(exercise.getDescription());
        System.out.println(exercise.getDefaultLength());
        if(exercise.getPreviewUrl() != null){
            exercisePreviewImg.setImageURI(Uri.parse(exercise.getPreviewUrl()));
        }
    }
}