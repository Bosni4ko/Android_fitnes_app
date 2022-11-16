package com.coursework.fitnessapp.exercises;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.ExerciseModel;

import java.io.Serializable;
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
        ExerciseModel exercise = exercises.get(position);
        holder.exerciseName.setText(exercises.get(position).getName());
        holder.exerciseCount.setText(exercises.get(position).getId().toString());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.parent.getContext(),AddToWorkoutExerciseActivity.class);
                intent.putExtra("exercise", String.valueOf(exercise.getId()));
                holder.parent.getContext().startActivity(intent);
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

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(itemView.getContext(),AddToWorkoutExerciseActivity.class);
//                    //intent.putExtra("exercise", String.valueOf(exercise));
//                    itemView.getContext().startActivity(intent);
//
//                }
//            });
        }
    }
}
