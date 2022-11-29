package com.coursework.fitnessapp.exercises;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.supportclasses.DurationFieldValidator;
import com.coursework.fitnessapp.supportclasses.InputFilterMinMax;
import com.coursework.fitnessapp.supportclasses.TimeDuration;
import com.google.android.material.textfield.TextInputLayout;

public class CreateExerciseActivity extends AppCompatActivity {

    private TextInputLayout exerciseNameLayout;
    private TextInputLayout exerciseDescriptionLayout;
    private TextInputLayout exerciseDurationLayout;
    private TextInputLayout exerciseCountLayout;

    private EditText exerciseName;
    private EditText exerciseDescription;
    private EditText exerciseCount;
    private ImageView exercisePreviewImg;

    private EditText txtHours;
    private EditText txtMinutes;
    private EditText txtSeconds;
    private ImageButton hoursArrowUp;
    private ImageButton hoursArrowDown;
    private ImageButton minutesArrowUp;
    private ImageButton minutesArrowDown;
    private ImageButton secondsArrowUp;
    private ImageButton secondsArrowDown;

    private Button addExerciseBtn;
    private Button backBtn;

    ExerciseModel exercise;
    DataBaseHelper dataBaseHelper;
    Boolean isEditMode = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        dataBaseHelper = new DataBaseHelper(CreateExerciseActivity.this);

        initLayout();
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            isEditMode = true;
            int id = Integer.parseInt(intent.getExtras().get("id").toString());
            exercise = dataBaseHelper.getExerciseById(id);
            setExerciseValues();
        }
    }
    private void initLayout(){
        exerciseName = findViewById(R.id.exerciseName);
        exerciseCount = findViewById(R.id.exerciseCount);
        exercisePreviewImg = findViewById(R.id.exercisePreviewImg);
        exerciseDescription = findViewById(R.id.exerciseDescription);

        exerciseNameLayout = findViewById(R.id.exerciseNameLayout);
        exerciseDescriptionLayout = findViewById(R.id.descriptionLayout);
        exerciseDurationLayout = findViewById(R.id.exerciseDurationLayout);
        exerciseCountLayout = findViewById(R.id.exerciseCountLayout);

        txtHours = findViewById(R.id.hours);
        txtMinutes = findViewById(R.id.minutes);
        txtSeconds = findViewById(R.id.seconds);
        txtHours.setFilters(new InputFilter[]{new InputFilterMinMax(0,59)});
        txtMinutes.setFilters(new InputFilter[]{new InputFilterMinMax(0,59)});
        txtSeconds.setFilters(new InputFilter[]{new InputFilterMinMax(0,59)});
        hoursArrowUp = findViewById(R.id.hoursArrowUp);
        hoursArrowDown = findViewById(R.id.hoursArrowDown);
        minutesArrowUp = findViewById(R.id.minutesArrowUp);
        minutesArrowDown = findViewById(R.id.minutesArrowDown);
        secondsArrowUp = findViewById(R.id.secondsArrowUp);
        secondsArrowDown = findViewById(R.id.secondsArrowDown);

        hoursArrowUp.setOnClickListener(changeTimeListener);
        hoursArrowDown.setOnClickListener(changeTimeListener);
        minutesArrowUp.setOnClickListener(changeTimeListener);
        minutesArrowDown.setOnClickListener(changeTimeListener);
        secondsArrowUp.setOnClickListener(changeTimeListener);
        secondsArrowDown.setOnClickListener(changeTimeListener);

        addExerciseBtn = findViewById(R.id.addExerciseBtn);
        backBtn = findViewById(R.id.backBtn);
        addExerciseBtn.setOnClickListener(addExercise);
        backBtn.setOnClickListener(back);
    }
    View.OnClickListener changeTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText text = txtHours;
            Boolean increase = false;
            switch(view.getId()){
                case R.id.hoursArrowUp:
                    text = txtHours;
                    increase = true;
                    break;
                case R.id.hoursArrowDown:
                    text = txtHours;
                    increase = false;
                    break;
                case R.id.minutesArrowUp:
                    text = txtMinutes;
                    increase = true;
                    break;
                case R.id.minutesArrowDown:
                    text = txtMinutes;
                    increase = false;
                    break;
                case R.id.secondsArrowUp:
                    text = txtSeconds;
                    increase = true;
                    break;
                case R.id.secondsArrowDown:
                    text = txtSeconds;
                    increase = false;
                    break;
            }
            int value;
            try{
                value = Integer.parseInt(String.valueOf(text.getText()));
            }
            catch (NumberFormatException e){
                value = 0;
            }
            if(increase && (value < 60)){
                value++;
            }
            else if(!increase && value > 0){
                value--;
            }
            String resultString = String.valueOf(value);
            if(resultString.length() < 2){
                resultString = "0" + resultString;
            }
            text.setText(resultString);
        }
    };
    View.OnClickListener addExercise = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            if(validateInput()){
                String name = exerciseName.getText().toString();
                String description = exerciseDescription.getText().toString();
                TimeDuration duration = new TimeDuration(txtHours.getText().toString(),txtMinutes.getText().toString(),txtSeconds.getText().toString());
                int count = Integer.parseInt(exerciseCount.getText().toString());
                ExerciseModel newExercise = new ExerciseModel(null,name,description,null,null,null,duration,count, Enums.ExerciseType.Custom.toString());
                if(isEditMode){
                    newExercise.setId(exercise.getId());
                    dataBaseHelper.editExercise(newExercise);
                }else {
                    dataBaseHelper.addExercise(newExercise);
                }
                finish();
            }
        }
    };
    View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    public void setExerciseValues(){
        exerciseName.setText(exercise.getName());
        exerciseCount.setText(String.valueOf(exercise.getDefaultCount()));
        exerciseDescription.setText(exercise.getDescription());
        txtHours.setText(exercise.getDefaultLength().getHours());
        txtMinutes.setText(exercise.getDefaultLength().getMinutes());
        txtSeconds.setText(exercise.getDefaultLength().getSeconds());

        addExerciseBtn.setText(R.string.save_changes);

    }
    private boolean validateInput(){
        boolean hasError = false;
        if(exerciseName.getText().toString().isEmpty()){
            exerciseNameLayout.setError(getResources().getString(R.string.empty_name_error));
            hasError = true;
        }else exerciseNameLayout.setError(null);
        if(exerciseDescription.getText().toString().isEmpty()){
            exerciseDescriptionLayout.setError(getResources().getString(R.string.empty_description_error));
            hasError = true;
        }else exerciseDescriptionLayout.setError(null);
        if(!DurationFieldValidator.validate(txtHours.getText().toString()) && !DurationFieldValidator.validate(txtMinutes.getText().toString()) && !DurationFieldValidator.validate(txtSeconds.getText().toString())){
            exerciseDurationLayout.setError(getResources().getString(R.string.zero_duration_error));
            hasError = true;
        }else exerciseDurationLayout.setError(null);
        if(!DurationFieldValidator.validate(exerciseCount.getText().toString())){
            exerciseCountLayout.setError(getResources().getString(R.string.zero_exercise_amount));
            hasError = true;
        }else exerciseCountLayout.setError(null);
        return !hasError;
    }
}