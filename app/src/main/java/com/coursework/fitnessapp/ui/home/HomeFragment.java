package com.coursework.fitnessapp.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.WorkoutSortComparator;
import com.coursework.fitnessapp.workout.CreateWorkoutActivity;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.databinding.FragmentHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class HomeFragment extends Fragment {
    private DataBaseHelper dataBaseHelper;

    private FragmentHomeBinding binding;
    private TextView todaysWorkoutsText;
    private TextView noWorkoutText;
    private RecyclerView workoutRecView;
    private FloatingActionButton addFab;
    private RelativeLayout notificationLayout;

    private ArrayList<WorkoutModel> workouts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBaseHelper = new DataBaseHelper(this.getContext());
        initLayout(view);
        setFragmentContent();

    }

    private void initLayout(View view){
        todaysWorkoutsText = view.findViewById(R.id.todaysWorkoutsText);
        noWorkoutText = view.findViewById(R.id.noWorkoutText);
        workoutRecView = view.findViewById(R.id.workoutsRecView);
        addFab = view.findViewById(R.id.addFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), CreateWorkoutActivity.class));
            }
        });
        notificationLayout = view.findViewById(R.id.notificationLayout);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFragmentContent(){
        workouts = dataBaseHelper.getWorkoutsByDate(new Date().toString());
        Collections.sort(workouts,new WorkoutSortComparator());
        if(!workouts.isEmpty()){
            todaysWorkoutsText.setVisibility(View.VISIBLE);
            noWorkoutText.setVisibility(View.GONE);
            WorkoutModel nextWorkout = workouts.get(0);
        }else {
            todaysWorkoutsText.setVisibility(View.GONE);
            noWorkoutText.setVisibility(View.VISIBLE);
        }
        if(dataBaseHelper.getAllUserWorkouts().isEmpty()){
            notificationLayout.setVisibility(View.GONE);
        }
        else {
            notificationLayout.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        setFragmentContent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}