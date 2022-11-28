package com.coursework.fitnessapp.ui.notifications;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.databinding.FragmentNotificationsBinding;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.exercises.CreateExerciseActivity;
import com.coursework.fitnessapp.exercises.ViewExercisesActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotificationsFragment extends Fragment {

    private Button defaultExercisesBtn;
    private Button customExercisesBtn;
    private FloatingActionButton addFab;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //NotificationsViewModel notificationsViewModel =
         //       new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.chooseExerciseTypeTitle;
        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        defaultExercisesBtn = view.findViewById(R.id.selectDefaultExerciseBtn);
        customExercisesBtn = view.findViewById(R.id.selectCustomExercisesBtn);
        defaultExercisesBtn.setOnClickListener(onTypeSelected);
        customExercisesBtn.setOnClickListener(onTypeSelected);
        addFab = view.findViewById(R.id.addFab);
        addFab.setOnClickListener(addExercise);

    }
    View.OnClickListener onTypeSelected = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            String type;
            Intent intent = new Intent(getContext(), ViewExercisesActivity.class);
            if(view.getId() == R.id.selectCustomExercisesBtn){
                type = Enums.ExerciseType.Custom.toString();
            }else {
                type = Enums.ExerciseType.Default.toString();
            }
            intent.putExtra("type",type);
            intent.putExtra("action",Enums.ExerciseAction.View);
            startActivity(intent);
        }
    };
    View.OnClickListener addExercise = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), CreateExerciseActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}