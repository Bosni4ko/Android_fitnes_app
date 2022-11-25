package com.coursework.fitnessapp.workout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;

import java.util.Collections;

public class ViewWorkoutActivity extends AppCompatActivity {
    private TextView name;
    private TextView date;
    private TextView time;
    private TextView duration;

    private TextView workoutDescription;
    private TextView workoutExpandedDescription;
    private ImageButton expandDescription;
    private ImageButton collapseDescription;
    private LinearLayout expandedDescriptionLayout;
    private LinearLayout collapsedDescriptionLayout;
    private Boolean isExpanded = false;

    private Button backBtn;
    private Button editBtn;

    private RecyclerView exercisesRecycleView;

    private DataBaseHelper dataBaseHelper;
    private WorkoutModel workout;
    ExercisesRecViewAdapter adapter;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workout);

        dataBaseHelper = new DataBaseHelper(ViewWorkoutActivity.this);

        Intent intent = getIntent();
        workout = dataBaseHelper.getWorkoutById(intent.getExtras().get("id").toString());
        initLayout();
        setWorkoutData();
    }

    private void initLayout(){
        name = findViewById(R.id.workoutName);
        date = findViewById(R.id.workoutDate);
        time = findViewById(R.id.workoutTime);
        duration = findViewById(R.id.workoutDuration);
        exercisesRecycleView = findViewById(R.id.exercisesRecycleView);

        workoutDescription = findViewById(R.id.workoutDescription);
        workoutExpandedDescription = findViewById(R.id.workoutExpandedDescription);
        expandDescription = findViewById(R.id.expandDescription);
        collapseDescription = findViewById(R.id.collapseDescription);

        expandedDescriptionLayout = findViewById(R.id.expandedDescriptionLayout);
        collapsedDescriptionLayout = findViewById(R.id.collapsedDescriptionLayout);
        expandDescription.setOnClickListener(changeDescription);
        collapseDescription.setOnClickListener(changeDescription);

        backBtn = findViewById(R.id.backBtn);
        editBtn = findViewById(R.id.editWorkoutBtn);
        backBtn.setOnClickListener(back);

    }
    View.OnClickListener changeDescription = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isExpanded && workoutDescription.getLineCount() > 2){
                expandedDescriptionLayout.setVisibility(View.VISIBLE);
                collapsedDescriptionLayout.setVisibility(View.GONE);
                isExpanded = !isExpanded;
            }
            else if(isExpanded && workoutDescription.getLineCount() > 2){
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
    View.OnClickListener editWorkout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWorkoutData(){
        name.setText(workout.getName());
        date.setText(workout.getDate().format(Enums.formatter));
        time.setText(workout.getTime().toString());
        Integer durationInt = 0;
        for (ExerciseModel exercise:workout.getExerciseModels()){
            durationInt = durationInt + exercise.getLength().getTimeInSeconds();
        }
        TimeDuration workoutTimeDuration = new TimeDuration(durationInt);
        duration.setText(workoutTimeDuration.getToStringDuration());
        setDescription();


        adapter = new ExercisesRecViewAdapter(false);
        adapter.setExercises(workout.getExerciseModels());
        exercisesRecycleView.setAdapter(adapter);
        exercisesRecycleView.setLayoutManager(new LinearLayoutManager(ViewWorkoutActivity.this));

    }
    private void setDescription(){
        workoutDescription.setText(workout.getDescription());
        workoutExpandedDescription.setText(workout.getDescription());
        workoutDescription.post(new Runnable() {
            @Override
            public void run() {
                if(workoutDescription.getLineCount() > 2){
                    workoutDescription.setMaxLines(2);
                    expandDescription.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}