package com.coursework.fitnessapp.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String EXERCISE_TABLE = "EXERCISE_TABLE";
    public static final String WORKOUT_TABLE = "WORKOUT_TABLE";
    public static final String COLUMN_URL = "URL";
    public static final String IMAGE_URL_TABLE = "IMAGE_" + COLUMN_URL + "_TABLE";
    public static final String WORKOUT_EXERCISES_TABLE = "WORKOUT_EXERCISES_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_VIDEO_URL = "VideoUrl";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_DEFAULT_COUNT = "defaultCount";
    public static final String COLUMN_SNUMBER = "SNumber";
    public static final String COLUMN_TYPE = "Type";



    public DataBaseHelper(@Nullable Context context) {
        super(context, "fitness_app.db" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createExerciseTableStatement = "CREATE TABLE " + EXERCISE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " nvarchar(20) NOT NULL, " + COLUMN_DESCRIPTION + " Text," + COLUMN_VIDEO_URL + " Text," + COLUMN_LENGTH + " INT NOT NULL," + COLUMN_DEFAULT_COUNT + " INT NOT NULL," + COLUMN_TYPE + " nvarchar(12) NOT NULL )";
        sqLiteDatabase.execSQL(createExerciseTableStatement);

        String createWorkoutTableStatement = "CREATE TABLE " + WORKOUT_TABLE + "(" + COLUMN_ID + " nvarchar(12) PRIMARY KEY NOT NULL," + COLUMN_NAME + " nvarchar(20) NOT NULL," + COLUMN_DESCRIPTION + " Text,Date Date NOT NULL,Status nvarchar(10) NOT NULL," + COLUMN_TYPE + " nvarchar(12) NOT NULL)";
        sqLiteDatabase.execSQL(createWorkoutTableStatement);

        String createImageUrlTableStatement = "CREATE TABLE " + IMAGE_URL_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_URL + " nvarchar(30)," + COLUMN_SNUMBER + " INT NOT NULL,Exercise_ID INTEGER NOT NULL,FOREIGN KEY(Exercise_ID) REFERENCES " + EXERCISE_TABLE + "(" +COLUMN_ID+"))";
        sqLiteDatabase.execSQL(createImageUrlTableStatement);

        String createWorkoutExercisesTableStatement = "CREATE TABLE " + WORKOUT_EXERCISES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SNUMBER + " INT NOT NULL," + COLUMN_LENGTH +" INT NOT NULL, Count INT NOT NULL,Workout_ID nvarchar(12) NOT NULL,Exercise_ID INTEGER NOT NULL,FOREIGN KEY(Workout_ID) REFERENCES " + WORKOUT_TABLE +"(" +COLUMN_ID +"),FOREIGN KEY(Exercise_ID) REFERENCES " + EXERCISE_TABLE +"(" +COLUMN_ID +")  )";
        sqLiteDatabase.execSQL(createWorkoutExercisesTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

//    public boolean fillDataBase(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        for(ExerciseModel exercise :exercises ) {
//            cv.put(COLUMN_NAME,exercise.getName());
//            cv.put(COLUMN_DESCRIPTION,exercise.getDescription());
//            if(exercise.getVideoUrl() != null) {
//                cv.put(COLUMN_VIDEO_URL,exercise.getVideoUrl());
//            }
//            cv.put(COLUMN_LENGTH,exercise.getLength().toString());
//            cv.put(COLUMN_DEFAULT_COUNT,exercise.getDefaultCount());
//        }
//        long insert = db.insert(EXERCISE_TABLE,null,cv);
//        if(insert == -1){
//            return false;
//        }
//        else{
//            return true;
//        }
//    }

    public boolean addExercise(ExerciseModel exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        ContentValues imgCv = new ContentValues();

        cv.put(COLUMN_NAME,exercise.getName());
        cv.put(COLUMN_DESCRIPTION,exercise.getDescription());
        if(exercise.getVideoUrl() != null) {
            cv.put(COLUMN_VIDEO_URL,exercise.getVideoUrl());
        }
        cv.put(COLUMN_LENGTH, String.valueOf(exercise.getLength()));
        cv.put(COLUMN_DEFAULT_COUNT,exercise.getDefaultCount());
        cv.put(COLUMN_TYPE,exercise.getType());
        long insert = db.insert(EXERCISE_TABLE,null,cv);
        if(insert == -1){
            return false;
        }
        else if(exercise.getVideoUrl() != null){
            int counter = 0;
            for(String imageUrl : exercise.getImageUrls()){
                imgCv.put(COLUMN_URL,imageUrl);
                imgCv.put(COLUMN_SNUMBER,counter);
                imgCv.put("Exercise_ID",exercise.getId());
                counter++;
            }
            insert = db.insert(IMAGE_URL_TABLE,null,imgCv);
            if(insert == -1){
                return false;
            }
            else {
                return true;
            }
        }
        else return true;
    }

    public boolean addWorkout(WorkoutModel workout){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        ContentValues wrkExCv = new ContentValues();

        cv.put(COLUMN_NAME,workout.getName());
        cv.put(COLUMN_DESCRIPTION,workout.getDescription());
        cv.put("Date", String.valueOf(workout.getDateTime()));
        cv.put("Status",workout.getStatus());
        cv.put(COLUMN_TYPE,workout.getType());

        long insert = db.insert(WORKOUT_TABLE,null,cv);
        if(insert == -1){
            return false;
        }
        else {
            int counter = 0;
            for (ExerciseModel exercise : workout.getExerciseModels()) {
                wrkExCv.put(COLUMN_SNUMBER, counter);
                wrkExCv.put(COLUMN_LENGTH, String.valueOf(exercise.getLength()));
                wrkExCv.put("Count", exercise.getCount());
                wrkExCv.put("Workout_id", workout.getId());
                wrkExCv.put("Exercise_ID", exercise.getId());
                counter++;
            }
            insert = db.insert(WORKOUT_TABLE,null,wrkExCv);
            if(insert == -1){
                return false;
            }
            else {
                return true;
            }
        }
    }

    public void fillDatabase(){
        ExerciseData exerciseData = new ExerciseData();
        for(ExerciseModel exercise: exerciseData.exercises){
            addExercise(exercise);
        }
    }

}
