package com.coursework.fitnessapp.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
        String createExerciseTableStatement = "CREATE TABLE " + EXERCISE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " nvarchar(20) NOT NULL, " + COLUMN_DESCRIPTION + " Text,PreviewImgURL Text," + COLUMN_VIDEO_URL + " Text," + COLUMN_LENGTH + " nvarchar(10) NOT NULL," + COLUMN_DEFAULT_COUNT + " INT NOT NULL," + COLUMN_TYPE + " nvarchar(12) NOT NULL )";
        sqLiteDatabase.execSQL(createExerciseTableStatement);

        String createWorkoutTableStatement = "CREATE TABLE " + WORKOUT_TABLE + "(" + COLUMN_ID + " nvarchar(12) PRIMARY KEY NOT NULL," + COLUMN_NAME + " nvarchar(20) NOT NULL," + COLUMN_DESCRIPTION + " Text,Date Date NOT NULL,Status nvarchar(10) NOT NULL," + COLUMN_TYPE + " nvarchar(12) NOT NULL)";
        sqLiteDatabase.execSQL(createWorkoutTableStatement);

        String createImageUrlTableStatement = "CREATE TABLE " + IMAGE_URL_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_URL + " nvarchar(30)," + COLUMN_SNUMBER + " INT NOT NULL,Exercise_ID INTEGER NOT NULL,FOREIGN KEY(Exercise_ID) REFERENCES " + EXERCISE_TABLE + "(" +COLUMN_ID+"))";
        sqLiteDatabase.execSQL(createImageUrlTableStatement);

        String createWorkoutExercisesTableStatement = "CREATE TABLE " + WORKOUT_EXERCISES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SNUMBER + " INT NOT NULL," + COLUMN_LENGTH +" INT NOT NULL, Count INT NOT NULL,Workout_ID nvarchar(12) NOT NULL,Exercise_ID INTEGER NOT NULL,FOREIGN KEY(Workout_ID) REFERENCES " + WORKOUT_TABLE +"(" +COLUMN_ID +"),FOREIGN KEY(Exercise_ID) REFERENCES " + EXERCISE_TABLE +"(" +COLUMN_ID +")  )";
        sqLiteDatabase.execSQL(createWorkoutExercisesTableStatement);

        //fillDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //fillDatabase();
    }


    public boolean addExercise(ExerciseModel exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        ContentValues imgCv = new ContentValues();

        cv.put(COLUMN_NAME,exercise.getName());
        cv.put(COLUMN_DESCRIPTION,exercise.getDescription());
        if(exercise.getPreviewUrl() != null){
            cv.put("PreviewImgURL",exercise.getPreviewUrl());
        }
        if(exercise.getVideoUrl() != null) {
            cv.put(COLUMN_VIDEO_URL,exercise.getVideoUrl());
        }
        cv.put(COLUMN_LENGTH, exercise.getDefaultLength().getToStringDuration());
        cv.put(COLUMN_DEFAULT_COUNT,exercise.getDefaultCount());
        cv.put(COLUMN_TYPE,exercise.getType());
        long insert = db.insert(EXERCISE_TABLE,null,cv);
        if(insert == -1){
            return false;
        }
        else if(exercise.getImageUrls() != null){
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

        cv.put(COLUMN_ID,workout.getId());
        cv.put(COLUMN_NAME,workout.getName());
        cv.put(COLUMN_DESCRIPTION,workout.getDescription());
        cv.put("Date", String.valueOf(workout.getDate()) +' '+ String.valueOf(workout.getTime()));
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
            insert = db.insert(WORKOUT_EXERCISES_TABLE,null,wrkExCv);
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

    public ArrayList<ExerciseModel> getAllExercisesOfType(String type){
        ArrayList<ExerciseModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + EXERCISE_TABLE + " WHERE " + COLUMN_TYPE + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, new String[]{type});

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String exerciseName = cursor.getString(1);
                String exerciseDescription = cursor.getString(2);
                TimeDuration exerciseLength = new TimeDuration(cursor.getString(4)) ;
                int exerciseCount = cursor.getInt(5);

                ExerciseModel newExercise = new ExerciseModel(id,exerciseName,exerciseDescription,null,null,null,exerciseLength,exerciseCount,type);
                returnList.add(newExercise);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public ExerciseModel getExerciseById(int id){
        ExerciseModel exercise;
        String queryString = "SELECT * FROM " + EXERCISE_TABLE + " WHERE " + COLUMN_ID + " = ?" ;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(id)});

        if(cursor.moveToFirst()){
            String exerciseName = cursor.getString(1);
            String exerciseDescription = cursor.getString(2);
            TimeDuration exerciseLength = new TimeDuration(cursor.getString(5)) ;
            int exerciseCount = cursor.getInt(6);
            String type = cursor.getString(7);

            exercise = new ExerciseModel(id,exerciseName,exerciseDescription,null,null,null,exerciseLength,exerciseCount,type);

        }
        else {
            exercise = new ExerciseModel();
        }
        cursor.close();
        db.close();
        return exercise;
    }

    public ArrayList<WorkoutModel> getAllWorkoutsWithStatus(String status){
        ArrayList<WorkoutModel> workouts = new ArrayList<WorkoutModel>();
        String queryString = "SELECT * FROM " + WORKOUT_TABLE + " WHERE Status = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{status});
        if(cursor.moveToFirst()){
            do {
                String id = cursor.getString(0);
                String workoutName = cursor.getString(1);
                String workoutDescription = cursor.getString(2);
                String dateTime = cursor.getString(3);
                String type = cursor.getString(5);
                LocalDate date = LocalDate.parse(dateTime.substring(0,9), Enums.formatter);
                LocalTime time= LocalTime.parse(dateTime.substring(11));

                WorkoutModel newWorkout = new WorkoutModel(id,workoutName,workoutDescription,getAllExercisesOfWorkout(id),date,time,type,status);
            }while (cursor.moveToNext());
        }
        return workouts;
    }
    public ArrayList<ExerciseModel> getAllExercisesOfWorkout(String workoutId){
        ArrayList<ExerciseModel> exercises = new ArrayList<ExerciseModel>();
        String queryString = "SELECT * FROM " + WORKOUT_EXERCISES_TABLE + " WHERE Workout_ID = ? ORDER BY " + COLUMN_SNUMBER + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{workoutId});
        if(cursor.moveToFirst()){
            do {
                ExerciseModel newExercise;
                TimeDuration duration = new TimeDuration(cursor.getString(2));
                Integer count = Integer.parseInt(cursor.getString(3));
                Integer exerciseId =  Integer.parseInt(cursor.getString(5));
                newExercise = getExerciseById(exerciseId);
                newExercise.setCount(count);
                newExercise.setLength(duration);
                exercises.add(newExercise);
            }while (cursor.moveToNext());
        }
        return exercises;
    }
}
