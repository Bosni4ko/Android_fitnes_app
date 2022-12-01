package com.coursework.fitnessapp.exercises;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.ExerciseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ExercisesRecViewAdapter extends RecyclerView.Adapter<ExercisesRecViewAdapter.ViewHolder> {
    private ArrayList<ExerciseModel> exercises;
    private String action;
    public ExercisesRecViewAdapter(String action) {
        this.action = action;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercises_add_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciseModel exercise = exercises.get(position);
        holder.exerciseName.setText(exercises.get(position).getName());
        holder.exerciseCount.setText(exercises.get(position).getId().toString());


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(action.equals(Enums.ExerciseAction.View.toString())){
                    Intent intent = new Intent(holder.parent.getContext(),ViewExerciseActivity.class);
                    intent.putExtra("exercise", String.valueOf(exercise.getId()));
                    holder.parent.getContext().startActivity(intent);
                }else {
                    Intent intent = new Intent(holder.parent.getContext(),AddToWorkoutExerciseActivity.class);
                    intent.putExtra("exercise", String.valueOf(exercise.getId()));
                    ((Activity) holder.parent.getContext()).startActivityForResult(intent,1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }


    public ArrayList<ExerciseModel> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<ExerciseModel> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView exerciseName;
        private TextView exerciseCount;
        private View parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseCount = itemView.findViewById(R.id.exerciseCount);

        }
    }
}
