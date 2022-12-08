package com.coursework.fitnessapp.exercises;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;

public class ViewExerciseActivity extends AppCompatActivity {

    private TextView exerciseName;
    private TextView exerciseCount;
    private ImageView exercisePreviewImg;
    private TextView exerciseDuration;
    private TextView exerciseDescription;
    private TextView exerciseExpandedDescription;
    private ImageButton expandDescription;
    private ImageButton collapseDescription;

    private LinearLayout expandedDescriptionLayout;
    private LinearLayout collapsedDescriptionLayout;
    private Boolean isExpanded = false;

    private Button backBtn;
    private Button editExerciseBtn;

    ExerciseModel exercise;
    DataBaseHelper dataBaseHelper;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        dataBaseHelper = new DataBaseHelper(ViewExerciseActivity.this);

        initLayout();
        Intent intent = getIntent();
        int id = Integer.parseInt(intent.getStringExtra("exercise"));

        exercise = dataBaseHelper.getExerciseById(id);
        setExerciseValues();
    }

    private void initLayout(){
        exerciseName = findViewById(R.id.exerciseName);
        exerciseDuration = findViewById(R.id.exerciseDuration);
        exerciseCount = findViewById(R.id.exerciseCount);
        exercisePreviewImg = findViewById(R.id.exercisePreviewImg);
        exerciseDescription = findViewById(R.id.exerciseDescription);
        exerciseExpandedDescription = findViewById(R.id.exerciseExpandedDescription);

        expandDescription = findViewById(R.id.expandDescription);
        collapseDescription = findViewById(R.id.collapseDescription);

        expandedDescriptionLayout = findViewById(R.id.expandedDescriptionLayout);
        collapsedDescriptionLayout = findViewById(R.id.collapsedDescriptionLayout);
        expandDescription.setOnClickListener(changeDescription);
        collapseDescription.setOnClickListener(changeDescription);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(back);
        editExerciseBtn = findViewById(R.id.editExerciseBtn);
        editExerciseBtn.setOnClickListener(editExercise);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setExerciseValues(){
        exerciseName.setText(exercise.getName());
        Intent intent = getIntent();
        if(intent.getStringExtra("duration") != null && intent.getStringExtra("count") != null){
            exerciseDuration.setText(intent.getStringExtra("duration"));
            exerciseCount.setText(String.valueOf(intent.getStringExtra("count")));
        }else {
            exerciseDuration.setText(exercise.getDefaultLength().getToStringDuration());
            exerciseCount.setText(String.valueOf(exercise.getCount()));
        }
        setDescription();
        if(exercise.getPreviewUrl() != null){
            exercisePreviewImg.setImageURI(Uri.parse(exercise.getPreviewUrl()));
        }
    }
    private void setDescription(){
        exerciseDescription.setText(exercise.getDescription());
        exerciseExpandedDescription.setText(exercise.getDescription());
        exerciseDescription.post(new Runnable() {
            @Override
            public void run() {
                if(exerciseDescription.getLineCount() > 2){
                    exerciseDescription.setMaxLines(2);
                    expandDescription.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    View.OnClickListener changeDescription = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isExpanded && exerciseDescription.getLineCount() > 2){
                expandedDescriptionLayout.setVisibility(View.VISIBLE);
                collapsedDescriptionLayout.setVisibility(View.GONE);
                isExpanded = !isExpanded;
            }
            else if(isExpanded && exerciseDescription.getLineCount() > 2) {
                expandedDescriptionLayout.setVisibility(View.GONE);
                collapsedDescriptionLayout.setVisibility(View.VISIBLE);
                isExpanded = !isExpanded;
            }
        }
    };
    View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
    View.OnClickListener editExercise = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ViewExerciseActivity.this,CreateExerciseActivity.class);
            intent.putExtra("id",exercise.getId());
            startActivity(intent);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        exercise = dataBaseHelper.getExerciseById(exercise.getId());
        setExerciseValues();
    }
}