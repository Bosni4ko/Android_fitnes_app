package com.coursework.fitnessapp.workout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;

import org.w3c.dom.Text;

import java.net.URI;

public class StartedWorkoutActivity extends AppCompatActivity {
    private ImageView previewImg;
    private TextView exerciseName;
    private TextView exerciseTimer;
    private TextView exerciseAmount;
    private TextView exerciseDescription;
    private TextView workoutTimer;

    private ImageButton playBtn;
    private ImageButton skipBtn;
    private ImageButton stopBtn;

    DataBaseHelper dataBaseHelper;
    WorkoutModel workout;
    ExerciseModel currentExercise;

    private boolean isStarted = false;
    private boolean isPaused = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_started_workout);
        dataBaseHelper = new DataBaseHelper(StartedWorkoutActivity.this);
        workout = dataBaseHelper.getWorkoutById((getIntent().getExtras().get("id")).toString());
        currentExercise = workout.getExerciseModels().get(0);
        System.out.println("Current exercise is:" + currentExercise);
        initLayout();
        setContent();
    }
    private void initLayout(){
        previewImg = findViewById(R.id.exercisePreviewImg);
        exerciseName = findViewById(R.id.exerciseName);
        exerciseTimer = findViewById(R.id.exerciseTimer);
        exerciseAmount = findViewById(R.id.exerciseAmount);
        exerciseDescription = findViewById(R.id.exerciseDescription);
        workoutTimer = findViewById(R.id.workoutTimer);

        playBtn = findViewById(R.id.startBtn);
        skipBtn = findViewById(R.id.skipBtn);
        stopBtn = findViewById(R.id.stopBtn);

        stopBtn.setOnClickListener(stopWorkout);
    }

    private void setContent(){
        if(currentExercise.getPreviewUrl() != null){
            previewImg.setImageURI(Uri.parse(currentExercise.getPreviewUrl()));
        }else previewImg.setImageResource(R.drawable.default_preview_img);
        exerciseName.setText(currentExercise.getName());
        exerciseTimer.setText(currentExercise.getLength().getToStringDuration());
        exerciseAmount.setText(String.valueOf(currentExercise.getCount()));
        exerciseDescription.setText(currentExercise.getDescription());
        workoutTimer.setText(calculateFullDuration().getToStringDuration());
    }
    private TimeDuration calculateFullDuration(){
        Integer durationInt = 0;
        for (ExerciseModel exercise:workout.getExerciseModels()){
            durationInt = durationInt + exercise.getLength().getTimeInSeconds();
        }
        return new TimeDuration(durationInt);
    }
    View.OnClickListener stopWorkout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder =  new AlertDialog.Builder(StartedWorkoutActivity.this);
            builder.setTitle(getResources().getString(R.string.stop_workout_title))
                    .setMessage(R.string.stop_workout_confirmation)
                    .setNegativeButton(R.string.stop, new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            workout.setStatus(Enums.WorkoutStatus.STOPPED.toString());
                            dataBaseHelper.editWorkout(workout);
                            finish();
                        }
                    }).setNeutralButton(R.string.back, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            builder.setCancelable(true);
                        }
                    }).show();
        }
    };
}