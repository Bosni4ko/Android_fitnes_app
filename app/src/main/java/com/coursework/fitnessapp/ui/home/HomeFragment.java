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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.WorkoutSortComparator;
import com.coursework.fitnessapp.workout.CreateWorkoutActivity;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    private DataBaseHelper dataBaseHelper;

    private FragmentHomeBinding binding;
    private TextView todaysWorkoutsText;
    private TextView noWorkoutText;
    private TextView nextWorkoutDate;
    private TextView nextWorkoutTime;
    private RecyclerView workoutRecView;
    private FloatingActionButton addFab;
    private RelativeLayout notificationLayout;
    private TodaysWorkoutsRecViewAdapter adapter;

    private ArrayList<WorkoutModel> workouts;
    private Thread thread;

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
        thread = new Thread(checkTime);
        thread.start();
    }

    private void initLayout(View view){
        todaysWorkoutsText = view.findViewById(R.id.todaysWorkoutsText);
        noWorkoutText = view.findViewById(R.id.noWorkoutText);
        workoutRecView = view.findViewById(R.id.workoutsRecView);
        nextWorkoutDate = view.findViewById(R.id.nextWorkoutDate);
        nextWorkoutTime= view.findViewById(R.id.nextWorkoutTime);
        addFab = view.findViewById(R.id.addFab);
        adapter = new TodaysWorkoutsRecViewAdapter();
        workoutRecView.setAdapter(adapter);
        workoutRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
        workouts = dataBaseHelper.getWorkoutsByDate(LocalDate.now().toString());
        Collections.sort(workouts,new WorkoutSortComparator());
        workouts.removeIf(workout -> workout.getDate().isAfter(LocalDate.now()));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!workouts.isEmpty()){
                    todaysWorkoutsText.setVisibility(View.VISIBLE);
                    noWorkoutText.setVisibility(View.GONE);

                }else {
                    todaysWorkoutsText.setVisibility(View.GONE);
                    noWorkoutText.setVisibility(View.VISIBLE);
                }
                adapter.setWorkouts(workouts);
            }
        });
    }

    Runnable checkTime = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            while (true) {
                ArrayList<WorkoutModel> allUserWorkouts = dataBaseHelper.getAllUserWorkouts();
                Collections.sort(allUserWorkouts, new WorkoutSortComparator());
                allUserWorkouts.removeIf(workout -> !workout.getStatus().equals(Enums.WorkoutStatus.WAITING.toString()));
                if (allUserWorkouts.isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            //TODO:notification
                            notificationLayout.setVisibility(View.GONE);
                        }
                    });
                } else {
                    WorkoutModel nextWorkout = allUserWorkouts.get(0);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notificationLayout.setVisibility(View.VISIBLE);
                        }
                    });
                    if (nextWorkout.getDate().format(Enums.formatter).equals(LocalDate.now().format(Enums.formatter))) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nextWorkoutDate.setText(R.string.today);
                            }
                        });
                        if (LocalTime.now().isAfter(nextWorkout.getTime())) {
                            if (nextWorkout.getTime().plusHours(1).isBefore(LocalTime.now())) {
                                System.out.println("Skipped because of now");
                                nextWorkout.setStatus(Enums.WorkoutStatus.SKIPPED.toString());
                                dataBaseHelper.editWorkout(nextWorkout);
                                setFragmentContent();
                                continue;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nextWorkoutTime.setText(R.string.now);
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nextWorkoutTime.setText(nextWorkout.getTime().toString());
                                }
                            });
                        }
                    } else if (nextWorkout.getDate().isBefore(LocalDate.now())) {
                        System.out.println("Skipped because of today");
                        nextWorkout.setStatus(Enums.WorkoutStatus.SKIPPED.toString());
                        dataBaseHelper.editWorkout(nextWorkout);
                        setFragmentContent();
                        continue;
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nextWorkoutDate.setText(nextWorkout.getDate().toString());
                                nextWorkoutTime.setText(nextWorkout.getTime().toString());
                            }
                        });
                    }

                }
                try {
                    synchronized (this) {
                        wait(5000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
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