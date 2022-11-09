package com.coursework.fitnessapp.DataBaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

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
    public static final String IMAGE_URL_TABLE = "IMAGE_URL_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_VIDEO_URL = "VideoUrl";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_DEFAULT_COUNT = "defaultCount";

    private Gson gson = new Gson();
    public Reader reader;

    {
        try {
            System.out.println("Path is: " + FileSystems.getDefault());
            reader = Files.newBufferedReader(Paths.get("exercises.json"));
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Reader failes");
            //TODO:something
        }
    }
    //public ArrayList<ExerciseModel> exercises= gson.fromJson(reader,new TypeToken<ArrayList<ExerciseModel>>(){}.getType());

    public DataBaseHelper(@Nullable Context context) {
        super(context, "fitness_app.db" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createExerciseTableStatement = "CREATE TABLE " + EXERCISE_TABLE + " (" + COLUMN_ID + " INT PRIMARY KEY AUTOINCREMENT NOT NULL, " + COLUMN_NAME + " nvarchar(20) NOT NULL, " + COLUMN_DESCRIPTION + " Text," + COLUMN_VIDEO_URL + " Text," + COLUMN_LENGTH + " INT NOT NULL," + COLUMN_DEFAULT_COUNT + " INT NOT NULL )";
        sqLiteDatabase.execSQL(createExerciseTableStatement);

        String createWorkoutTableStatement = "CREATE TABLE " + WORKOUT_TABLE + "(" + COLUMN_ID + " nvarchar(12) PRIMARY KEY NOT NULL," + COLUMN_NAME + " nvarchar(20) NOT NULL,Date Date NOT NULL,Status nvarchar(10) NOT NULL)";
        sqLiteDatabase.execSQL(createWorkoutTableStatement);

        String createImageUrlTableStatement = "CREATE TABLE " + IMAGE_URL_TABLE + " (" + COLUMN_ID + " INT PRIMARY KEY AUTOINCREMENT NOT NULL,URL nvarchar(30),FOREIGN key(Exercise_ID) REFERENCES EXERCISE_TABLE(ID))";
        sqLiteDatabase.execSQL(createImageUrlTableStatement);
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
}
