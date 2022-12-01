package com.coursework.fitnessapp.workout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.exercises.ViewExercisesActivity;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CreateWorkoutActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityResultLauncher;
    DataBaseHelper dataBaseHelper;
    private EditText workoutName;
    private EditText workoutDescription;

    private TextInputLayout workoutNameLayout;
    private TextInputLayout dateTimeLayout;
    private TextInputLayout workoutDescriptionLayout;
    private TextInputLayout exercisesLayout;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button timeButton;
    private Button addWorkout;
    int hour;
    int minute;
    TimePickerDialog timePickerDialog;
    private RecyclerView exercisesRecycleView;

    private Dialog dialog;
    private Button addDefaultExercise;
    private Button addCustomExercise;
    private Boolean isEditMode;

    private ArrayList<ExerciseModel> exercises;
    private WorkoutModel workout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        dataBaseHelper = new DataBaseHelper(CreateWorkoutActivity.this);
        Intent intent = getIntent();
        if(intent.getExtras() == null){
            exercises = new ArrayList<>();
            isEditMode = false;
        }
        else {
            isEditMode = true;
            workout = dataBaseHelper.getWorkoutById(intent.getExtras().get("id").toString());
            exercises = workout.getExerciseModels();
        }
        ExercisesRecViewAdapter adapter = new ExercisesRecViewAdapter();
        adapter.setExercises(exercises);
        initLayout(adapter);


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                dialog.cancel();
                if(result.getData() != null){
                    Bundle bundle = result.getData().getExtras();
                    ExerciseModel newExercise = dataBaseHelper.getExerciseById(Integer.parseInt(bundle.get("id").toString()));
                    newExercise.setCount(Integer.parseInt(bundle.get("count").toString()));
                    newExercise.setLength(new TimeDuration(bundle.get("length").toString()));
                    exercises.add(newExercise);
                    adapter.setExercises(exercises);
                }
            }
        });
    }

    private void initLayout(ExercisesRecViewAdapter adapter){
        workoutName = findViewById(R.id.workoutName);
        workoutDescription = findViewById(R.id.workoutDescription);
        workoutNameLayout = findViewById(R.id.workoutNameLayout);
        dateTimeLayout = findViewById(R.id.dateTimeLayout);
        workoutDescriptionLayout = findViewById(R.id.workoutDescriptionLayout);
        exercisesLayout = findViewById(R.id.exercisesLayout);

        dateButton = findViewById(R.id.datePickerBtn);
        initDatePicker();

        timeButton = findViewById(R.id.timePickerBtn);
        initTimePicker();

        exercisesRecycleView = findViewById(R.id.exercisesRecycleView);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int positionDragged = dragged.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                Collections.swap(exercises,positionDragged,positionTarget);
                adapter.notifyItemMoved(positionDragged,positionTarget);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(exercisesRecycleView);
        exercisesRecycleView.setAdapter(adapter);
        exercisesRecycleView.setLayoutManager(new LinearLayoutManager(CreateWorkoutActivity.this));

        addWorkout = findViewById(R.id.addWorkoutBtn);

        if(isEditMode){
            dateButton.setText(workout.getDate().format(Enums.formatter));
            workoutName.setText(workout.getName());
            workoutDescription.setText(workout.getDescription());
            timeButton.setText(workout.getTime().toString());
            addWorkout.setText(R.string.save_changes);
        }
        else {
            dateButton.setText(getTodaysDate());
        }
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = workoutName.getText().toString();
                String description = workoutDescription.getText().toString();
                LocalDate date = LocalDate.parse(dateButton.getText().toString(),Enums.formatter);
                LocalTime time = LocalTime.parse(timeButton.getText().toString());

                String generatedString = String.valueOf(Math.floor(Math.random() * (9*Math.pow(10,9))) + Math.pow(10,(9)));
                String id;
                if(name.length() > 10){
                    id = name.substring(0,9) + generatedString;
                }
                else{
                    id = name + generatedString;
                }
                String status = Enums.WorkoutStatus.WAITING.toString();
                String type = Enums.WorkoutType.CUSTOM.toString();

                WorkoutModel newWorkout;
                if(validateInput()){
                    if(isEditMode){
                        newWorkout = new WorkoutModel(workout.getId(),name,description,exercises,date,time,type,status);
                        dataBaseHelper.editWorkout(newWorkout);
                    }
                    else {
                        newWorkout = new WorkoutModel(id,name,description,exercises,date,time,type,status);
                        dataBaseHelper.addWorkout(newWorkout);
                    }
                    finish();
                }
            }
        });

    }
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day,month,year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(CreateWorkoutActivity.this,style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day,int month,int year){
        String dayString = String.valueOf(day);
        String monthString = String.valueOf(month);
        if(day < 10){
            dayString = "0" + dayString;
        }
        if(month < 10){
            monthString = "0" + monthString;
        }
        return dayString + " " + monthString + " " + year;
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private void initTimePicker(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
            }
        };
        timeButton.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
        int style = AlertDialog.THEME_HOLO_LIGHT;
        timePickerDialog = new TimePickerDialog(CreateWorkoutActivity.this,style,onTimeSetListener,hour,minute,true);
    }
    public void openTimePicker(View view) {
        timePickerDialog.setTitle("Select time");
        timePickerDialog.show();
    }

    public void addExercise(View view) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.choose_exercise_popup);

        addDefaultExercise = dialog.findViewById(R.id.selectDefaultExercisesBtn);
        addCustomExercise = dialog.findViewById(R.id.selectCustomExercisesBtn);
        addDefaultExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateWorkoutActivity.this, ViewExercisesActivity.class);
                intent.putExtra("type",Enums.ExerciseType.Default);
                intent.putExtra("action",Enums.ExerciseAction.Add);
                activityResultLauncher.launch(intent);
            }
        });
        addCustomExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateWorkoutActivity.this, ViewExercisesActivity.class);
                intent.putExtra("type",Enums.ExerciseType.Custom);
                intent.putExtra("action",Enums.ExerciseAction.Add);
                activityResultLauncher.launch(intent);
            }
        });
        dialog.show();
    }
    public boolean validateInput(){
        Boolean hasError = false;
        if(workoutName.getText().toString().isEmpty()){
            workoutNameLayout.setError(getResources().getString(R.string.empty_name_error));
            hasError = true;
        }
        else workoutNameLayout.setError(null);
        LocalDate setDate = LocalDate.parse(dateButton.getText().toString(),Enums.formatter);
        LocalTime setTime = LocalTime.parse(timeButton.getText().toString());
        if(setDate.isBefore(LocalDate.now()) || (setDate.equals(LocalDate.now()) && setTime.isBefore(LocalTime.now()))){
            dateTimeLayout.setError(getResources().getString(R.string.incorrect_date_time_error));
            hasError = true;
        }
        else dateTimeLayout.setError(null);
        if(workoutDescription.getText().toString().isEmpty()){
            workoutDescriptionLayout.setError(getResources().getString(R.string.empty_description_error));
            hasError = true;
        }else workoutDescriptionLayout.setError(null);
        if(exercises.isEmpty()){
            exercisesLayout.setError(getResources().getString(R.string.no_exercises_error));
        }else exercisesLayout.setError(null);
        return !hasError;
    }

}