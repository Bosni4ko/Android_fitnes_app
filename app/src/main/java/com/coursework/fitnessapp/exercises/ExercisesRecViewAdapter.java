package com.coursework.fitnessapp.exercises;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.ExerciseModel;

import java.util.ArrayList;

public class ExercisesRecViewAdapter extends RecyclerView.Adapter<ExercisesRecViewAdapter.ViewHolder> {

    private ArrayList<ExerciseModel> exercises;
    public ExercisesRecViewAdapter() {
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
        holder.exerciseName.setText(exercises.get(position).getName());
        holder.exerciseCount.setText(exercises.get(position).getId().toString());
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseCount = itemView.findViewById(R.id.exerciseCount);
        }
    }
}
