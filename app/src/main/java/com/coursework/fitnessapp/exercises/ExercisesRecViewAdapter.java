package com.coursework.fitnessapp.exercises;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.InternalStoragePhoto;
import com.coursework.fitnessapp.models.WorkoutModel;

import java.util.ArrayList;

//#ExercisesRecViewAdapter adapter is responsible for showing the list of exercises
public class ExercisesRecViewAdapter extends RecyclerView.Adapter<ExercisesRecViewAdapter.ViewHolder> {
    private ArrayList<ExerciseModel> exercises;
    private final String action;
    private final String type;
    Context context;
    DataBaseHelper dataBaseHelper;
    public ExercisesRecViewAdapter(String action,String type) {
        this.action = action;
        this.type = type;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercises_add_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        context = parent.getContext();
        dataBaseHelper = new DataBaseHelper(context);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //#Get exercise of current position
        ExerciseModel exercise = exercises.get(position);
        //#Set exercise data
        holder.exerciseName.setText(exercise.getName());
        holder.exerciseLength.setText(exercise.getDefaultLength().getToStringDuration());
        if(exercise.getPreviewImageName()!= null){
            holder.exercisePreviewImg.setImageBitmap(InternalStoragePhoto.loadImageFromInternalStorage(context,exercise.getPreviewImageName()).get(0).getBmp());
        }
        //#If exercise type is default don't show remove button
        if(type.equals(Enums.ExerciseType.Default.toString())){
            holder.removeExerciseBtn.setVisibility(View.GONE);
        }
        //#Listener for deleting exercises from the list
        else {
            holder.removeExerciseBtn.setOnClickListener(view -> {
                //#Show dialog to confirm exercise deletion
                AlertDialog.Builder builder =  new AlertDialog.Builder(holder.parent.getContext());
                builder.setTitle("Are you sure")
                        .setNegativeButton(R.string.delete, (dialogInterface, i) -> {
                            //#Check if exercise is in waiting workout
                            Boolean inWorkout = false;
                            for (WorkoutModel workout:dataBaseHelper.getAllUserWorkouts()) {
                               if(workout.getStatus().equals(Enums.WorkoutStatus.WAITING.toString())){
                                    for (ExerciseModel exerciseModel:workout.getExerciseModels()){
                                        if(exerciseModel.getId().equals(exercise.getId())){
                                            inWorkout = true;
                                            break;
                                        }
                                    }
                                }
                                if (inWorkout){
                                    break;
                                }
                            }
                            //#Delete exercise and images from the DB and internal storage
                            if(!inWorkout && dataBaseHelper.deleteExercise(exercise.getId())){
                                InternalStoragePhoto.deleteImageFromInternalStorage(exercise.getPreviewImageName()+".jpg",context);
                                if(exercise.getImageNames() != null){
                                    for (String image:exercise.getImageNames()) {
                                        InternalStoragePhoto.deleteImageFromInternalStorage(image+".jpg",context);
                                    }
                                }
                                exercises.remove(exercise);
                                notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(context, R.string.delete_exercise_error,Toast.LENGTH_LONG).show();
                            }
                        }).setNeutralButton(R.string.back, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                builder.setCancelable(true);
                            }
                        }).show();
            });
        }

        //#Listener to open exercise detailed view
        holder.parent.setOnClickListener(new View.OnClickListener() {
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

    //#Initialise viewholder layout
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView exerciseName;
        private final TextView exerciseLength;
        private final ImageView exercisePreviewImg;
        private final ImageButton removeExerciseBtn;
        private final View parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseLength = itemView.findViewById(R.id.exerciseLength);
            exercisePreviewImg = itemView.findViewById(R.id.exercisePreviewImg);
            removeExerciseBtn = itemView.findViewById(R.id.removeExercise);

        }
    }
}
