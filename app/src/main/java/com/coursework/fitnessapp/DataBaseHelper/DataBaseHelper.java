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
import com.coursework.fitnessapp.models.SavedWorkoutProgressModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String EXERCISE_TABLE = "EXERCISE_TABLE";
    public static final String WORKOUT_TABLE = "WORKOUT_TABLE";
    public static final String CURRENT_WORKOUT_TABLE = "CURRENT_WORKOUT_TABLE";
    public static final String IMAGE_NAME_TABLE = "IMAGE_NAME_TABLE";
    public static final String WORKOUT_EXERCISES_TABLE = "WORKOUT_EXERCISES_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_VIDEO_URL = "VideoUrl";
    public static final String COLUMN_DURATION = "Duration";
    public static final String COLUMN_COUNT = "Count";
    public static final String COLUMN_DEFAULT_COUNT = "default" + COLUMN_COUNT;
    public static final String COLUMN_SNUMBER = "SNumber";
    public static final String COLUMN_TYPE = "Type";
    public static final String COLUMN_PREVIEW_IMG_URL = "PreviewImgURL";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_EXERCISE_ID = "Exercise_ID";
    public static final String COLUMN_WORKOUT_ID = "Workout_ID";
    public static final String COLUMN_USER_EMAIL = "User_Email";

    private GoogleSignInAccount account;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "fitness_app.db" , null, 1);
        account = GoogleSignIn.getLastSignedInAccount(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createExerciseTableStatement = "CREATE TABLE " + EXERCISE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " nvarchar(30) NOT NULL, " + COLUMN_DESCRIPTION + " Text NOT NULL," + COLUMN_PREVIEW_IMG_URL + " Text," + COLUMN_VIDEO_URL + " Text," + COLUMN_DURATION + " nvarchar(8) NOT NULL," + COLUMN_DEFAULT_COUNT + " INT NOT NULL," + COLUMN_TYPE + " nvarchar(12) NOT NULL," + COLUMN_USER_EMAIL + " nvarchar(320) )";
        sqLiteDatabase.execSQL(createExerciseTableStatement);

        String createWorkoutTableStatement = "CREATE TABLE " + WORKOUT_TABLE + "(" + COLUMN_ID + " nvarchar(12) PRIMARY KEY NOT NULL," + COLUMN_NAME + " nvarchar(30) NOT NULL," + COLUMN_DESCRIPTION + " Text NOT NULL," + COLUMN_DATE + " Date NOT NULL," + COLUMN_STATUS + " nvarchar(10) NOT NULL," + COLUMN_TYPE + " nvarchar(12) NOT NULL," + COLUMN_USER_EMAIL + " nvarchar(320) NOT NULL)";
        sqLiteDatabase.execSQL(createWorkoutTableStatement);

        String createImageUrlTableStatement = "CREATE TABLE " + IMAGE_NAME_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_NAME + " Text," + COLUMN_SNUMBER + " INT NOT NULL," + COLUMN_EXERCISE_ID + " INTEGER NOT NULL,FOREIGN KEY("+COLUMN_EXERCISE_ID+") REFERENCES " + EXERCISE_TABLE + "(" +COLUMN_ID+"))";
        sqLiteDatabase.execSQL(createImageUrlTableStatement);

        String createWorkoutExercisesTableStatement = "CREATE TABLE " + WORKOUT_EXERCISES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SNUMBER + " INT NOT NULL," + COLUMN_DURATION + " INT NOT NULL, " + COLUMN_COUNT + " INT NOT NULL," + COLUMN_WORKOUT_ID + " nvarchar(12) NOT NULL," + COLUMN_EXERCISE_ID + " INTEGER NOT NULL,FOREIGN KEY(" + COLUMN_WORKOUT_ID + ") REFERENCES " + WORKOUT_TABLE +"(" +COLUMN_ID + "),FOREIGN KEY(" + COLUMN_EXERCISE_ID + ") REFERENCES " + EXERCISE_TABLE +"(" +COLUMN_ID +")  )";
        sqLiteDatabase.execSQL(createWorkoutExercisesTableStatement);

        String createCurrentWorkoutTableStatement = "CREATE TABLE " + CURRENT_WORKOUT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Current_exercise_index INT NOT NULL, Current_exercise_timer INT NOT NULL, Current_workout_timer INT NOT NULL," + COLUMN_WORKOUT_ID + " nvarchar(12) NOT NULL,FOREIGN KEY(" + COLUMN_WORKOUT_ID + ") REFERENCES " + WORKOUT_TABLE +"(" +COLUMN_ID + ")) ";
        sqLiteDatabase.execSQL(createCurrentWorkoutTableStatement);

        //fillDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }


    public boolean addExercise(ExerciseModel exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        ContentValues imgCv = new ContentValues();

        cv.put(COLUMN_NAME,exercise.getName());
        cv.put(COLUMN_DESCRIPTION,exercise.getDescription());
        if(exercise.getPreviewImageName() != null){
            cv.put(COLUMN_PREVIEW_IMG_URL,exercise.getPreviewImageName());
        }
        if(exercise.getVideoUrl() != null) {
            cv.put(COLUMN_VIDEO_URL,exercise.getVideoUrl());
        }
        cv.put(COLUMN_DURATION, exercise.getDefaultLength().getToStringDuration());
        cv.put(COLUMN_DEFAULT_COUNT,exercise.getDefaultCount());
        cv.put(COLUMN_TYPE,exercise.getType());
        System.out.println();
        if(exercise.getType().equals( Enums.ExerciseType.Custom.toString())){
            cv.put(COLUMN_USER_EMAIL,account.getEmail());
        }
        long insert = db.insert(EXERCISE_TABLE,null,cv);
        if(insert == -1){
            return false;
        }
        else if(exercise.getImageNames() != null){
            Cursor cursor = db.rawQuery("SELECT last_insert_rowid()",new String[]{});
            cursor.moveToFirst();
            exercise.setId(cursor.getInt(0));
            int counter = 0;
            System.out.println("Exercises are: "  + exercise.getImageNames());
            for(String imageName : exercise.getImageNames()){
                imgCv.put(COLUMN_NAME,imageName);
                imgCv.put(COLUMN_SNUMBER,counter);
                imgCv.put(COLUMN_EXERCISE_ID,exercise.getId());
                counter++;
                insert = db.insert(IMAGE_NAME_TABLE,null,imgCv);
                if(insert == -1){
                    return false;
                }
            }
            return true;
        }
        else return true;
    }

    public boolean deleteExercise(int id){
        SQLiteDatabase db = getWritableDatabase();
        System.out.println(!getExerciseById(id).getType().equals(Enums.ExerciseType.Default.toString()));
        System.out.println(exerciseBelongToWorkout(id));
        if(!getExerciseById(id).getType().equals(Enums.ExerciseType.Default.toString()) && !exerciseBelongToWorkout(id)){
            db.delete(EXERCISE_TABLE, COLUMN_ID + " = ?",new String[]{String.valueOf(id)});
            return true;
        }
        else return false;

    }
    public boolean addWorkout(WorkoutModel workout){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        ContentValues wrkExCv = new ContentValues();

        cv.put(COLUMN_ID,workout.getId());
        cv.put(COLUMN_NAME,workout.getName());
        cv.put(COLUMN_DESCRIPTION,workout.getDescription());
        cv.put(COLUMN_DATE, String.valueOf(workout.getDate()) +' '+ workout.getTime().toString());
        cv.put(COLUMN_STATUS,workout.getStatus());
        cv.put(COLUMN_TYPE,workout.getType());
        if(workout.getType() == Enums.WorkoutType.CUSTOM.toString()){
            cv.put(COLUMN_USER_EMAIL,account.getEmail());
        }
        long insert = db.insert(WORKOUT_TABLE,null,cv);
        if(insert == -1){
            return false;
        }
        else {
            int counter = 0;
            for (ExerciseModel exercise : workout.getExerciseModels()) {
                wrkExCv.put(COLUMN_SNUMBER, counter);
                wrkExCv.put(COLUMN_DURATION, exercise.getLength().getToStringDuration());
                wrkExCv.put(COLUMN_COUNT, exercise.getCount());
                wrkExCv.put(COLUMN_WORKOUT_ID, workout.getId());
                wrkExCv.put(COLUMN_EXERCISE_ID, exercise.getId());
                counter++;
                insert = db.insert(WORKOUT_EXERCISES_TABLE,null,wrkExCv);
                if(insert == -1){
                    return false;
                }
            }
            return true;
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(type.equals(Enums.ExerciseType.Default.toString())){
            String queryString = "SELECT * FROM " + EXERCISE_TABLE + " WHERE " + COLUMN_TYPE + " = ?";
            cursor = db.rawQuery(queryString, new String[]{type});
        }
        else {
            String queryString = "SELECT * FROM " + EXERCISE_TABLE + " WHERE " + COLUMN_TYPE + " = ? AND " + COLUMN_USER_EMAIL + " = ?";
            cursor = db.rawQuery(queryString, new String[]{type,account.getEmail()});
        }

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String exerciseName = cursor.getString(1);
                String exerciseDescription = cursor.getString(2);
                String previewImageName = cursor.getString(3);
                TimeDuration exerciseLength = new TimeDuration(cursor.getString(5)) ;
                int exerciseCount = cursor.getInt(6);
                ExerciseModel newExercise = new ExerciseModel(id,exerciseName,exerciseDescription,previewImageName,null,null,exerciseLength,exerciseCount,type);
                returnList.add(newExercise);
            }while(cursor.moveToNext());
        }
        cursor.close();
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
            String previewImageName = cursor.getString(3);
            TimeDuration exerciseLength = new TimeDuration(cursor.getString(5)) ;
            int exerciseCount = cursor.getInt(6);
            String type = cursor.getString(7);

            ArrayList<String> imageNames = new ArrayList<>();
            queryString = "SELECT * FROM " + IMAGE_NAME_TABLE + " WHERE " + COLUMN_EXERCISE_ID + " = ? ORDER BY " + COLUMN_SNUMBER + " ASC" ;
            cursor = db.rawQuery(queryString, new String[]{String.valueOf(id)});
            if(cursor.moveToFirst()){
                do {
                    imageNames.add(cursor.getString(1));
;                }while (cursor.moveToNext());
            }
            exercise = new ExerciseModel(id,exerciseName,exerciseDescription,previewImageName,imageNames,null,exerciseLength,exerciseCount,type);

        }
        else {
            exercise = new ExerciseModel();
        }
        cursor.close();
        return exercise;
    }
    public void editExercise(ExerciseModel exercise){
        if(exercise.getType() != Enums.ExerciseType.Default.toString()){
            SQLiteDatabase db = getWritableDatabase();
            String queryString = "DELETE FROM " + IMAGE_NAME_TABLE + " WHERE " + COLUMN_EXERCISE_ID + " = ?";
            db.execSQL(queryString,new String[]{String.valueOf(exercise.getId())});
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME,exercise.getName());
            cv.put(COLUMN_DESCRIPTION,exercise.getDescription());
            cv.put(COLUMN_PREVIEW_IMG_URL,exercise.getPreviewImageName());
            cv.put(COLUMN_VIDEO_URL,exercise.getVideoUrl());
            cv.put(COLUMN_DURATION,exercise.getDefaultLength().getToStringDuration());
            cv.put(COLUMN_DEFAULT_COUNT,exercise.getDefaultCount());
            cv.put(COLUMN_TYPE,exercise.getType());
            db.update(EXERCISE_TABLE,cv,COLUMN_ID + " = ?",new String[]{String.valueOf(exercise.getId())});
            ContentValues imgCv = new ContentValues();
            if(exercise.getImageNames() != null){
                int counter = 0;
                for(String imageName : exercise.getImageNames()) {
                    imgCv.put(COLUMN_NAME, imageName);
                    imgCv.put(COLUMN_SNUMBER, counter);
                    imgCv.put(COLUMN_EXERCISE_ID, exercise.getId());
                    counter++;
                    db.insert(IMAGE_NAME_TABLE, null, imgCv);
                }
            }
        }

    }
    public WorkoutModel getWorkoutById(String id){
        WorkoutModel workout;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String queryString = "SELECT * FROM " + WORKOUT_TABLE + " WHERE " + COLUMN_ID + " = ? AND " + COLUMN_USER_EMAIL + " = ?" ;
        cursor = db.rawQuery(queryString, new String[]{id,account.getEmail()});

        if(cursor.moveToFirst()){
            String workoutName = cursor.getString(1);
            String workoutDescription = cursor.getString(2);
            String dateTime = cursor.getString(3);
            LocalDate date = LocalDate.parse(dateTime.substring(0,10), Enums.fromDbFormatter);
            LocalTime time = LocalTime.parse(dateTime.substring(11));
            String status = cursor.getString(4);
            String type = cursor.getString(5);
            ArrayList<ExerciseModel> exercises = getAllExercisesOfWorkout(id);
            workout = new WorkoutModel(id,workoutName,workoutDescription,exercises,date,time,type,status);
        }
        else workout = new WorkoutModel();
        cursor.close();
        db.close();
        return workout;
    }

    public ArrayList<WorkoutModel> getAllUserWorkouts(){
        ArrayList<WorkoutModel> workouts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + WORKOUT_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(queryString, new String[]{account.getEmail()});
        WorkoutModel newWorkout;
        if(cursor.moveToFirst()){
            do {
                String id = cursor.getString(0);
                String workoutName = cursor.getString(1);
                String workoutDescription = cursor.getString(2);
                String dateTime = cursor.getString(3);
                LocalDate date = LocalDate.parse(dateTime.substring(0,10), Enums.fromDbFormatter);
                LocalTime time = LocalTime.parse(dateTime.substring(11));
                String status = cursor.getString(4);
                String type = cursor.getString(5);
                ArrayList<ExerciseModel> exercises = getAllExercisesOfWorkout(id);
                newWorkout = new WorkoutModel(id,workoutName,workoutDescription,exercises,date,time,type,status);
                workouts.add(newWorkout);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  workouts;
    }
    public ArrayList<WorkoutModel> getWorkoutsByDate(String workoutDate){
        workoutDate = String.format(workoutDate, Enums.formatter);
        ArrayList<WorkoutModel> workouts = new ArrayList<>();
        WorkoutModel newWorkout;
        String queryString = "SELECT * FROM " + WORKOUT_TABLE + " WHERE " + COLUMN_DATE + " >= ? AND " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_STATUS + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{workoutDate + " 00:00",account.getEmail(),Enums.WorkoutStatus.WAITING.toString()});
        if(cursor.moveToFirst()){
            do {
                String id = cursor.getString(0);
                String workoutName = cursor.getString(1);
                String workoutDescription = cursor.getString(2);
                String dateTime = cursor.getString(3);
                LocalDate date = LocalDate.parse(dateTime.substring(0,10), Enums.fromDbFormatter);
                LocalTime time = LocalTime.parse(dateTime.substring(11));
                String status = cursor.getString(4);
                String type = cursor.getString(5);
                ArrayList<ExerciseModel> exercises = getAllExercisesOfWorkout(id);
                newWorkout = new WorkoutModel(id,workoutName,workoutDescription,exercises,date,time,type,status);
                workouts.add(newWorkout);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return workouts;
    }
    public void editWorkout(WorkoutModel workout){
        SQLiteDatabase db = getWritableDatabase();
        String queryString = "DELETE FROM " + WORKOUT_EXERCISES_TABLE + " WHERE " + COLUMN_WORKOUT_ID + " = ?";
        db.execSQL(queryString,new String[]{workout.getId()});
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,workout.getName());
        cv.put(COLUMN_DESCRIPTION,workout.getDescription());
        cv.put(COLUMN_DATE,workout.getDate().toString() + ' ' + workout.getTime().toString());
        cv.put(COLUMN_STATUS,workout.getStatus());
        db.update(WORKOUT_TABLE,cv,  COLUMN_ID + " = ?",new String[]{workout.getId()});

        ContentValues wrkExCv = new ContentValues();
        int counter = 0;
        for (ExerciseModel exercise : workout.getExerciseModels()) {
            System.out.println("Iteration: " + counter);
            wrkExCv.put(COLUMN_SNUMBER, counter);
            wrkExCv.put(COLUMN_DURATION, exercise.getLength().getToStringDuration());
            wrkExCv.put(COLUMN_COUNT, exercise.getCount());
            wrkExCv.put(COLUMN_WORKOUT_ID, workout.getId());
            wrkExCv.put(COLUMN_EXERCISE_ID, exercise.getId());
            counter++;
            db.insert(WORKOUT_EXERCISES_TABLE,null,wrkExCv);
        }

    }
    public ArrayList<WorkoutModel> getAllWorkoutsWithStatus(String status){
        ArrayList<WorkoutModel> workouts = new ArrayList<WorkoutModel>();
        String queryString = "SELECT * FROM " + WORKOUT_TABLE + " WHERE " + COLUMN_STATUS + " = ? AND " + COLUMN_USER_EMAIL + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{status,account.getEmail()});
        if(cursor.moveToFirst()){
            do {
                String id = cursor.getString(0);
                String workoutName = cursor.getString(1);
                String workoutDescription = cursor.getString(2);
                String dateTime = cursor.getString(3);
                String type = cursor.getString(5);
                LocalDate date = LocalDate.parse(dateTime.substring(0,10), Enums.fromDbFormatter);
                LocalTime time= LocalTime.parse(dateTime.substring(11));

                WorkoutModel newWorkout = new WorkoutModel(id,workoutName,workoutDescription,getAllExercisesOfWorkout(id),date,time,type,status);
                workouts.add(newWorkout);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return workouts;
    }
    public ArrayList<ExerciseModel> getAllExercisesOfWorkout(String workoutId){
        ArrayList<ExerciseModel> exercises = new ArrayList<>();
        String queryString = "SELECT * FROM " + WORKOUT_EXERCISES_TABLE + " WHERE " + COLUMN_WORKOUT_ID + " = ? ORDER BY " + COLUMN_SNUMBER + " ASC";
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
        cursor.close();
        return exercises;
    }
    public boolean exerciseBelongToWorkout(int id){
        String queryString = "SELECT * FROM " + WORKOUT_EXERCISES_TABLE + " WHERE " + COLUMN_EXERCISE_ID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(id)});
        if(cursor.moveToFirst()){
            return true;
        }
        else return false;
    }
    public void deleteWorkout(String workoutId){
        String queryString = "DELETE FROM " + WORKOUT_EXERCISES_TABLE + " WHERE " + COLUMN_WORKOUT_ID + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString,new String[]{workoutId});

        queryString = "DELETE FROM " + WORKOUT_TABLE + " WHERE " + COLUMN_ID + " = ?";
        db.execSQL(queryString,new String[]{workoutId});
    }

    public void addCurrentWorkout(SavedWorkoutProgressModel savedWorkoutProgressModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Current_exercise_index",savedWorkoutProgressModel.getExerciseIndex());
        cv.put("Current_exercise_timer",savedWorkoutProgressModel.getExTimer());
        cv.put("Current_workout_timer",savedWorkoutProgressModel.getWrkTimer());
        cv.put(COLUMN_WORKOUT_ID, savedWorkoutProgressModel.getWorkoutId());

        db.insert(CURRENT_WORKOUT_TABLE,null,cv);
    }

    public SavedWorkoutProgressModel getCurrentWorkout(){
        SavedWorkoutProgressModel savedWorkoutProgress = savedWorkoutProgress = new SavedWorkoutProgressModel();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + CURRENT_WORKOUT_TABLE;
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            savedWorkoutProgress.setExerciseIndex(cursor.getInt(1));
            savedWorkoutProgress.setExTimer(cursor.getInt(2));
            savedWorkoutProgress.setWrkTimer(cursor.getInt(3));
            savedWorkoutProgress.setWorkoutId(cursor.getString(4));
        }

        cursor.close();
        return savedWorkoutProgress;
    }
    public void deleteCurrentWorkouts(){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CURRENT_WORKOUT_TABLE;
        db.execSQL(queryString);
    }
}
