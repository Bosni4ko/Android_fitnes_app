package com.coursework.fitnessapp.ui.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;
import com.coursework.fitnessapp.workout.ViewWorkoutActivity;

import java.util.ArrayList;

public class WorkoutsRecViewAdapter extends RecyclerView.Adapter<WorkoutsRecViewAdapter.ViewHolder>{
    private ArrayList<WorkoutModel> workouts = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;
    public WorkoutsRecViewAdapter(){
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public WorkoutsRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workouts_list,parent,false);
        WorkoutsRecViewAdapter.ViewHolder holder = new WorkoutsRecViewAdapter.ViewHolder(view);
        dataBaseHelper = new DataBaseHelper(parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutsRecViewAdapter.ViewHolder holder, int position) {


        WorkoutModel workout = workouts.get(position);
        holder.workoutName.setText(workout.getName());
        holder.workoutDate.setText(workout.getDate().toString());
        holder.workoutTime.setText(workout.getTime().toString());
        Integer duration = 0;
        for (ExerciseModel exercise:workout.getExerciseModels()){
            duration = duration + exercise.getLength().getTimeInSeconds();
        }
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
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.parent.getContext(), ViewWorkoutActivity.class);
                intent.putExtra("id",workout.getId());
                ((Activity) holder.parent.getContext()).startActivity(intent);
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
        private TextView workoutDate;
        private TextView workoutTime;
        private TextView workoutDuration;
        private ImageButton removeWorkout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            workoutName = itemView.findViewById(R.id.workoutName);
            workoutDate = itemView.findViewById(R.id.workoutDate);
            workoutTime = itemView.findViewById(R.id.workoutTime);
            workoutDuration = itemView.findViewById(R.id.workoutDuration);
            removeWorkout = itemView.findViewById(R.id.removeWorkout);
        }
    }
}
