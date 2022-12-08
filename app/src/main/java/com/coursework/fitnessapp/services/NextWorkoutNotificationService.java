package com.coursework.fitnessapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.MainActivity;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.WorkoutSortComparator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;


public class NextWorkoutNotificationService extends Service {

    private String channelId = "Workout notification channel";
    Context context;
    DataBaseHelper dataBaseHelper;
    NotificationChannel channel;
    public NextWorkoutNotificationService(){

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        dataBaseHelper = new DataBaseHelper(this);
        new Thread(sendNotifications).start();
        final String CHANNEL_ID = "Workout service";
        channel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID,NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(context,CHANNEL_ID)
                .setContentText("Workout notifications are running");
        startForeground(1001,notification.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable sendNotifications= new Runnable(){
        WorkoutModel notifiedWorkout;
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            while (true){
                try {
                    synchronized (this){
                        wait(5000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ArrayList<WorkoutModel> workouts = dataBaseHelper.getWorkoutsByDate(LocalDate.now().toString());
                Collections.sort(workouts,new WorkoutSortComparator());
                checkForSkippedWorkouts(workouts);
                if(!workouts.isEmpty()){
                    WorkoutModel workout = workouts.get(0);
                    if(workout.getTime().minusMinutes(15).isBefore(LocalTime.now())){
                        if(notifiedWorkout != null && notifiedWorkout.getId().equals(workout.getId())){}
                        else {
                            Notification.Builder builder = new Notification.Builder(context,channelId)
                                    .setContentTitle(getResources().getString(R.string.workout_notification_title))
                                    .setContentText(workout.getName() + " " + getResources().getString(R.string.workout_notification_at) + " " + workout.getTime())
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.ic_app_foreground);
                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                            managerCompat.notify(1,builder.build());
                            notifiedWorkout = workout;
                        }
                    }
                }

            }
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        private ArrayList<WorkoutModel> checkForSkippedWorkouts(ArrayList<WorkoutModel> workouts){
            while(!workouts.isEmpty() && workouts.get(0).getTime().isAfter(LocalTime.now().plusHours(1))){
                workouts.get(0).setStatus(Enums.WorkoutStatus.SKIPPED.toString());
                dataBaseHelper.editWorkout(workouts.get(0));
                workouts.remove(0);
            }
            return workouts;
        }
    };
}
