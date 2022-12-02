package com.coursework.fitnessapp.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;
import com.coursework.fitnessapp.workout.ViewWorkoutActivity;

import java.util.ArrayList;

public class TodaysWorkoutsRecViewAdapter extends RecyclerView.Adapter<TodaysWorkoutsRecViewAdapter.ViewHolder>{
    private ArrayList<WorkoutModel> workouts = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;
    public TodaysWorkoutsRecViewAdapter(){
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public TodaysWorkoutsRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todays_workout_list,parent,false);
        TodaysWorkoutsRecViewAdapter.ViewHolder holder = new TodaysWorkoutsRecViewAdapter.ViewHolder(view);
        dataBaseHelper = new DataBaseHelper(parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodaysWorkoutsRecViewAdapter.ViewHolder holder, int position) {
        WorkoutModel workout = workouts.get(position);
        holder.expandedLayout.setVisibility(View.GONE);
        holder.workoutName.setText(workout.getName());
        holder.workoutTime.setText(workout.getTime().toString());
        Integer duration = 0;
        for (ExerciseModel exercise:workout.getExerciseModels()){
            duration = duration + exercise.getLength().getTimeInSeconds();
        }
        holder.workoutDescription.setText(workout.getDescription());
        holder.workoutStatus.setText(workout.getStatus());
        TimeDuration workoutTimeDuration = new TimeDuration(duration);
        holder.workoutDuration.setText(workoutTimeDuration.getToStringDuration());
        holder.removeWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =  new AlertDialog.Builder(holder.parent.getContext());
                builder.setTitle("Are you sure")
                        .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                workouts.remove(workout);
                                setWorkouts(workouts);
                                dataBaseHelper.deleteWorkout(workout.getId());
                            }
                        }).setNeutralButton(R.string.back, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                builder.setCancelable(true);
                            }
                        }).show();
            }
        });
        holder.collapsedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.isExpanded){
                    holder.expandedLayout.setVisibility(View.GONE);
                }
                else {
                    holder.expandedLayout.setVisibility(View.VISIBLE);
                }
                holder.isExpanded = !holder.isExpanded;
            }
        });
        holder.startWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.parent.getContext(),ViewWorkoutActivity.class);
                intent.putExtra("id",workout.getId());
                intent.putExtra("action", Enums.WorkoutAction.Start.toString());
                holder.parent.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }
    public void setWorkouts(ArrayList<WorkoutModel> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View parent;
        private TextView workoutName;
        private TextView workoutTime;
        private TextView workoutDuration;
        private TextView workoutDescription;
        private TextView workoutStatus;
        private ImageButton removeWorkout;
        private ImageButton startWorkoutBtn;
        private RelativeLayout collapsedLayout;
        private RelativeLayout expandedLayout;
        private boolean isExpanded = false;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            workoutName = itemView.findViewById(R.id.workoutName);
            workoutTime = itemView.findViewById(R.id.workoutTime);
            workoutDuration = itemView.findViewById(R.id.workoutDuration);
            workoutStatus = itemView.findViewById(R.id.workoutStatus);
            removeWorkout = itemView.findViewById(R.id.removeWorkout);
            workoutDescription = itemView.findViewById(R.id.workoutDescription);
            startWorkoutBtn = itemView.findViewById(R.id.startWorkoutBtn);
            collapsedLayout = itemView.findViewById(R.id.collapsedRelativeLayout);
            expandedLayout = itemView.findViewById(R.id.expandedRelativeLayout);
        }
    }
}
