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

//#Service which send notification about next workout and remove outdated workouts
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
        //#Create notification channel and start it on foreground
        context = this;
        dataBaseHelper = new DataBaseHelper(this);
        new Thread(sendNotifications).start();
        final String CHANNEL_ID = "Workout service";
        channel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID,NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(context,CHANNEL_ID)
                .setContentText("Workout notifications are running").setAutoCancel(true);
        startForeground(1001,notification.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //#Thread which check workouts and send notification
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
                //get all workouts with today's date and sort them
                ArrayList<WorkoutModel> workouts = dataBaseHelper.getWorkoutsByDate(LocalDate.now().toString());
                Collections.sort(workouts,new WorkoutSortComparator());
                workouts.removeIf(workout -> workout.getDate().isAfter(LocalDate.now()));
                checkForSkippedWorkouts(workouts);
                //if workout is after 15 minutes or less and no notification about this workout was sent,send a notification about it
                if(!workouts.isEmpty()){
                    WorkoutModel workout = workouts.get(0);
                    if(workout.getTime().minusMinutes(15).isBefore(LocalTime.now())){
                        if(notifiedWorkout != null && notifiedWorkout.getId().equals(workout.getId())){}
                        else {
                            Notification.Builder builder = new Notification.Builder(context,channelId)
                                    .setContentTitle(getResources().getString(R.string.workout_notification_title))
                                    .setContentText(workout.getName() + " " + getResources().getString(R.string.workout_notification_at) + " " + workout.getTime())
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.app_icon);
                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                            managerCompat.notify(1,builder.build());
                            notifiedWorkout = workout;
                        }
                    }
                }

            }
        }

        //#Change first workout status to skipped if you skipp workout at least for an hour
        @RequiresApi(api = Build.VERSION_CODES.O)
        private ArrayList<WorkoutModel> checkForSkippedWorkouts(ArrayList<WorkoutModel> workouts){
            while(!workouts.isEmpty() && workouts.get(0).getTime().plusHours(1).isBefore(LocalTime.now())){
                workouts.get(0).setStatus(Enums.WorkoutStatus.SKIPPED.toString());
                dataBaseHelper.editWorkout(workouts.get(0));
                workouts.remove(0);
            }
            return workouts;
        }
    };
}
