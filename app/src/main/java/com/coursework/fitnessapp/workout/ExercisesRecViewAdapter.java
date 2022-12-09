package com.coursework.fitnessapp.workout;

import android.app.Activity;
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

import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.exercises.AddToWorkoutExerciseActivity;
import com.coursework.fitnessapp.exercises.ViewExerciseActivity;
import com.coursework.fitnessapp.models.ExerciseModel;

import java.util.ArrayList;

public class ExercisesRecViewAdapter extends RecyclerView.Adapter<ExercisesRecViewAdapter.ViewHolder>{

    private ArrayList<ExerciseModel> exercises = new ArrayList<>();
    private boolean hasRemoveBtn = true;
    public ExercisesRecViewAdapter() {
    }
    public ExercisesRecViewAdapter(Boolean hasRemoveBtn) {
        this.hasRemoveBtn = hasRemoveBtn;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercises_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciseModel exercise = exercises.get(position);
        holder.exerciseName.setText(exercise.getName());
        holder.exerciseDuration.setText(exercise.getLength().getToStringDuration());
        holder.exerciseCount.setText(String.valueOf(exercise.getCount()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.parent.getContext(), ViewExerciseActivity.class);
                intent.putExtra("exercise", String.valueOf(exercise.getId()));
                intent.putExtra("duration",exercise.getLength().getToStringDuration());
                intent.putExtra("count",String.valueOf(exercise.getCount()));holder.parent.getContext().startActivity(intent);
            }
        });
        if(hasRemoveBtn){
            holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exercises.remove(exercise);
                    notifyDataSetChanged();
                }
            });
        }else holder.removeBtn.setVisibility(View.GONE);

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
        private TextView exerciseDuration;
        private TextView exerciseCount;
        private ImageButton removeBtn;
        private View parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            exerciseName = itemView.findViewById(R.id.txtExerciseName);
            exerciseDuration = itemView.findViewById(R.id.exerciseDuration);
            exerciseCount = itemView.findViewById(R.id.exerciseCount);
            removeBtn = itemView.findViewById(R.id.removeExercise);
        }
    }
}
