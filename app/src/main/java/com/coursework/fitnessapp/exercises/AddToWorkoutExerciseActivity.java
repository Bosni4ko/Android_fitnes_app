package com.coursework.fitnessapp.exercises;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
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
import com.coursework.fitnessapp.models.InternalStoragePhoto;
import com.coursework.fitnessapp.supportclasses.InputFilterMinMax;
import com.google.android.material.textfield.TextInputLayout;

//#Activity for adding exercise to workout
public class AddToWorkoutExerciseActivity extends AppCompatActivity {

    private TextView exerciseName;
    private EditText exerciseCount;
    private ImageView exercisePreviewImg;
    private TextView exerciseDescription;
    private TextView exerciseExpandedDescription;
    private ImageButton expandDescription;
    private ImageButton collapseDescription;

    private LinearLayout expandedDescriptionLayout;
    private LinearLayout collapsedDescriptionLayout;
    private Boolean isExpanded = false;

    private EditText txtHours;
    private EditText txtMinutes;
    private EditText txtSeconds;
    private ImageButton hoursArrowUp;
    private ImageButton hoursArrowDown;
    private ImageButton minutesArrowUp;
    private ImageButton minutesArrowDown;
    private ImageButton secondsArrowUp;
    private ImageButton secondsArrowDown;
    private RecyclerView imagesRecView;

    private Button addExerciseBtn;

    private TextInputLayout errorLayout;

    ExerciseModel exercise;
    ViewImagesRecViewAdapter adapter = new ViewImagesRecViewAdapter();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(AddToWorkoutExerciseActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_workout_exercise);
        initLayout();
        //#Get exercise id from intent
        Intent intent = getIntent();
        int id = Integer.parseInt(intent.getStringExtra("exercise"));

        exercise = dataBaseHelper.getExerciseById(id);
        setExerciseValues(exercise);
    }

    //#Initialise layout
    private void initLayout(){
        exerciseName = findViewById(R.id.exerciseName);
        exerciseCount = findViewById(R.id.exerciseCount);
        exercisePreviewImg = findViewById(R.id.exercisePreviewImg);
        exerciseDescription = findViewById(R.id.exerciseDescription);
        exerciseExpandedDescription = findViewById(R.id.exerciseExpandedDescription);
        imagesRecView = findViewById(R.id.exerciseImages);
        imagesRecView.setAdapter(adapter);
        imagesRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

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
        expandDescription = findViewById(R.id.expandDescription);
        collapseDescription = findViewById(R.id.collapseDescription);

        expandedDescriptionLayout = findViewById(R.id.expandedDescriptionLayout);
        collapsedDescriptionLayout = findViewById(R.id.collapsedDescriptionLayout);
        expandDescription.setOnClickListener(changeDescription);
        collapseDescription.setOnClickListener(changeDescription);

        errorLayout = findViewById(R.id.errorLayout);

        hoursArrowUp.setOnClickListener(changeTimeListener);
        hoursArrowDown.setOnClickListener(changeTimeListener);
        minutesArrowUp.setOnClickListener(changeTimeListener);
        minutesArrowDown.setOnClickListener(changeTimeListener);
        secondsArrowUp.setOnClickListener(changeTimeListener);
        secondsArrowDown.setOnClickListener(changeTimeListener);

        addExerciseBtn = findViewById(R.id.addExerciseBtn);
        addExerciseBtn.setOnClickListener(addExercise);
    }
    //#Set increase/decrease time function on arrow buttons
    View.OnClickListener changeTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText text = txtHours;
            boolean increase = false;
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
            resultString = formatTimeValue(resultString);
            text.setText(resultString);
        }
    };
    //#Expand/collapse exercise description function
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
    //#Add exercise to workout function
    View.OnClickListener addExercise = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(validateValues()){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("id",exercise.getId().toString());
                returnIntent.putExtra("length",formatTimeValue(txtHours.getText().toString()) + ":" + formatTimeValue(txtMinutes.getText().toString()) + ":" + formatTimeValue(txtSeconds.getText().toString()));
                returnIntent.putExtra("count",exerciseCount.getText().toString());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }
    };

    //# Format time(hours/minutes or seconds) into 2 digit format
    private String formatTimeValue(String timeValue){
        while(timeValue.length() < 2){
            timeValue = "0" + timeValue;
        }
        return timeValue;
    }
    //#Validate input values
    private boolean validateValues(){
        boolean isValid = true;
        if((!editFieldIsValid(txtHours) && !editFieldIsValid(txtMinutes) && !editFieldIsValid(txtSeconds)) || !editFieldIsValid(exerciseCount)){
            errorLayout.setError(getString(R.string.wrong_values));
            isValid = false;
        }
        return isValid;
    }

    //# Validate edit field
    private boolean editFieldIsValid(EditText field){
        boolean isValid = true;
        if(field.getText().toString().equals("") || (Integer.parseInt(field.getText().toString()) < 1)){
            isValid = false;
        }
        return  isValid;
    }

    //#Set all exercise values
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setExerciseValues(ExerciseModel exercise){
        exerciseName.setText(exercise.getName());
        txtHours.setText(exercise.getDefaultLength().getHours());
        txtMinutes.setText(exercise.getDefaultLength().getMinutes());
        txtSeconds.setText(exercise.getDefaultLength().getSeconds());
        exerciseCount.setText(String.valueOf(exercise.getDefaultCount()));
        setDescription(exercise);
        if(exercise.getPreviewImageName() != null){
            exercisePreviewImg.setImageBitmap(InternalStoragePhoto.loadImageFromInternalStorage(AddToWorkoutExerciseActivity.this,exercise.getPreviewImageName()).get(0).getBmp());
        }
        if(exercise.getImageNames() != null && !exercise.getImageNames().isEmpty()){
            adapter.setImages(exercise.getImageNames());
        }
    }

    //# Set exercise description
    private void setDescription(ExerciseModel exercise){
        exerciseDescription.setText(exercise.getDescription());
        exerciseExpandedDescription.setText(exercise.getDescription());
        exerciseDescription.post(() -> {
            if(exerciseDescription.getLineCount() > 2){
                exerciseDescription.setMaxLines(2);
                expandDescription.setVisibility(View.VISIBLE);
            }
        });
    }
}