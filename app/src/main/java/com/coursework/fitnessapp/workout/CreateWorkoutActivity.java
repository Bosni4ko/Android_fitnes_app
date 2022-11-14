package com.coursework.fitnessapp.workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.exercises.DefaultExerciseActivity;
import com.coursework.fitnessapp.models.ExerciseModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CreateWorkoutActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button timeButton;
    private Button addExerciseButton;
    int hour;
    int minute;
    TimePickerDialog timePickerDialog;
    private RecyclerView exercisesRecycleView;

    private Dialog dialog;
    private Button addDefaultExercise;
    private Button addCustomExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        initDatePicker();
        dateButton = findViewById(R.id.datePickerBtn);
        dateButton.setText(getTodaysDate());

        initTimePicker();
        timeButton = findViewById(R.id.timePickerBtn);

        ArrayList<ExerciseModel> exercises = new ArrayList<>();
        ExercisesRecViewAdapter adapter = new ExercisesRecViewAdapter();
        adapter.setExercises(exercises);
        exercisesRecycleView = findViewById(R.id.exercisesRecycleView);
        exercisesRecycleView.setAdapter(adapter);
        exercisesRecycleView.setLayoutManager(new LinearLayoutManager(CreateWorkoutActivity.this));

        addExerciseButton = findViewById(R.id.addExerciseBtn);
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
                startActivity(intent);
            }
        });
        addCustomExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.show();
//        Intent intent = new Intent(CreateWorkoutActivity.this, ChooseExerciseTypeDialogActivity.class);
//        startActivity(intent);
    }

}