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
import android.widget.TextView;
import android.widget.TimePicker;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.exercises.DefaultExerciseActivity;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CreateWorkoutActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityResultLauncher;
    DataBaseHelper dataBaseHelper = new DataBaseHelper(CreateWorkoutActivity.this);
    private EditText workoutName;
    private EditText workoutDescription;
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

    private ArrayList<ExerciseModel> exercises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        exercises = new ArrayList<>();
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

        initDatePicker();
        dateButton = findViewById(R.id.datePickerBtn);
        dateButton.setText(getTodaysDate());

        initTimePicker();
        timeButton = findViewById(R.id.timePickerBtn);

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


                WorkoutModel workout = new WorkoutModel(id,name,description,exercises,date,time,type,status);
                dataBaseHelper.addWorkout(workout);
                finish();
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
        return day + " " + month + " " + year;
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
                Intent intent = new Intent(CreateWorkoutActivity.this, DefaultExerciseActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
        addCustomExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.show();
    }

}