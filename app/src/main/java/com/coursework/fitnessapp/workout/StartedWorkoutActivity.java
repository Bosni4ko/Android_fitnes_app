package com.coursework.fitnessapp.workout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import com.coursework.fitnessapp.models.SavedWorkoutProgressModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.concurrent.TimeUnit;

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
    private boolean isPaused = true;
    private boolean finishedWorkout = false;
    private int exTimer;
    private int wrkTimer;
    TimeDuration workoutDuration;
    private Thread workoutThread;
    private Runnable r;
    private MediaPlayer mediaPlayer;
    private boolean isRunningTask;
    private int counter;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_started_workout);
        dataBaseHelper = new DataBaseHelper(StartedWorkoutActivity.this);
        workout = dataBaseHelper.getWorkoutById((getIntent().getExtras().get("id")).toString());
        currentExercise = workout.getExerciseModels().get(0);
        initLayout();
        setContent();
        mediaPlayer = MediaPlayer.create(StartedWorkoutActivity.this, R.raw.simple_countdown_beep );
        r = new Runnable() {
            @Override
            public void run() {
                isRunningTask = true;
                workoutDuration = new TimeDuration(wrkTimer);
                while (true){
                    try {
                        synchronized (this){
                            wait(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!isPaused) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!isRunningTask){
                                    isRunningTask = true;
                                    setContent();
                                    //TODO:play sound
                                }
                                else {
                                    exerciseTimer.setText(currentExercise.getLength().getToStringDuration());
                                    workoutTimer.setText(workoutDuration.getToStringDuration());
                                }
                            }
                        });
                        if (exTimer > 0) {
                            exTimer--;
                            wrkTimer--;
                            currentExercise.getLength().setTime(exTimer);
                            workoutDuration.setTime(wrkTimer);
                        } else if((workout.getExerciseModels().indexOf(currentExercise)) <= workout.getExerciseModels().size()-2){
                            currentExercise = workout.getExerciseModels().get(workout.getExerciseModels().indexOf(currentExercise) + 1);
                            exTimer = currentExercise.getLength().getTimeInSeconds();
                            taskBreak.run();
                            isRunningTask = false;
                            //TODO:play sound
                        }else {
                            //TODO:finish workout
                            dataBaseHelper.deleteCurrentWorkouts();
                        }
                    }
                }
            }
        };
        workoutThread = new Thread(r,"WorkoutProcess");
    }
    Runnable taskBreak = new Runnable() {
        @Override
        public void run() {
            counter = 5;
            while(counter >= 0){
                //TODO:tick sound and change style
                mediaPlayer.start();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        exerciseTimer.setText(String.valueOf(counter));
                        workoutTimer.setText(workoutDuration.getToStringDuration());
                    }
                });
                try {
                    synchronized (this){
                        wait(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter--;
                wrkTimer--;
                workoutDuration.setTime(wrkTimer);
            }
            wrkTimer++;
            workoutDuration.setTime(wrkTimer);
        }
    };
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

        playBtn.setOnClickListener(startWorkout);
        skipBtn.setOnClickListener(skipExercise);
        stopBtn.setOnClickListener(stopWorkout);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setContent(){
        SavedWorkoutProgressModel savedWorkoutProgress = dataBaseHelper.getCurrentWorkout();
        if(workout.getId().equals(savedWorkoutProgress.getWorkoutId())){
            currentExercise = workout.getExerciseModels().get(savedWorkoutProgress.getExerciseIndex());
            exTimer = savedWorkoutProgress.getExTimer();
            wrkTimer = savedWorkoutProgress.getWrkTimer();
            workoutDuration = new TimeDuration(wrkTimer);
            currentExercise.getLength().setTime(exTimer);
            workoutTimer.setText(workoutDuration.getToStringDuration());
        }else{
            exTimer = currentExercise.getLength().getTimeInSeconds();
            if(!isStarted){
                wrkTimer = calculateFullDuration().getTimeInSeconds();
                workoutTimer.setText(calculateFullDuration().getToStringDuration());
            }else {
                workoutTimer.setText(workoutDuration.getToStringDuration());
            }
        }
        exerciseTimer.setText(currentExercise.getLength().getToStringDuration());
        if(currentExercise.getPreviewUrl() != null){
            previewImg.setImageURI(Uri.parse(currentExercise.getPreviewUrl()));
        }else previewImg.setImageResource(R.drawable.default_preview_img);
        exerciseName.setText(currentExercise.getName());
        exerciseAmount.setText(String.valueOf(currentExercise.getCount()));
        exerciseDescription.setText(currentExercise.getDescription());
        dataBaseHelper.deleteCurrentWorkouts();
    }
    private TimeDuration calculateFullDuration(){
        Integer durationInt = 0;
        for (ExerciseModel exercise:workout.getExerciseModels()){
            durationInt = durationInt + exercise.getLength().getTimeInSeconds();
        }
        durationInt = durationInt + (workout.getExerciseModels().size() - 1 - workout.getExerciseModels().indexOf(currentExercise)) * 5;
        return new TimeDuration(durationInt);
    }

    View.OnClickListener startWorkout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isStarted){
                workoutThread.start();
                isStarted = true;
            }
            if(isPaused){
                playBtn.setImageResource(R.drawable.ic_pause_icon);
            }else{
                playBtn.setImageResource(R.drawable.ic_play_arrow);
            }
            isPaused = !isPaused;
        }
    };
    View.OnClickListener skipExercise = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isStarted && workout.getExerciseModels().indexOf(currentExercise) != (workout.getExerciseModels().size() - 1) && isRunningTask){
                wrkTimer = wrkTimer - exTimer;
                workoutDuration.setTime(wrkTimer);
                exTimer = 0;
            }
        }
    };
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();
        saveCurrentProgress();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveCurrentProgress();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveCurrentProgress(){
        if(isStarted && !finishedWorkout){
            if(!isRunningTask){
                wrkTimer = wrkTimer - counter;
            }
            SavedWorkoutProgressModel savedWorkoutProgress = new SavedWorkoutProgressModel(workout.getExerciseModels().indexOf(currentExercise),exTimer,wrkTimer,workout.getId());
            dataBaseHelper.addCurrentWorkout(savedWorkoutProgress);
        }
    }
}