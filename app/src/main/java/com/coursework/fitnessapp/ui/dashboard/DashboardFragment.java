package com.coursework.fitnessapp.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.databinding.FragmentDashboardBinding;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.WorkoutModel;
import com.coursework.fitnessapp.supportclasses.WorkoutSortComparator;

import java.util.ArrayList;
import java.util.Collections;

//#Fragment for showing workout list
@RequiresApi(api = Build.VERSION_CODES.O)
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private TextView workoutsText;
    private RecyclerView workoutsRecView;
    private WorkoutsRecViewAdapter adapter;
    private DataBaseHelper dataBaseHelper;
    ArrayList<WorkoutModel> workouts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBaseHelper = new DataBaseHelper(this.getContext());

        adapter = new WorkoutsRecViewAdapter();
        workoutsRecView = view.findViewById(R.id.workoutsRecView);
        workoutsText = view.findViewById(R.id.workoutsText);
        setFragmentContent();
        workoutsRecView.setAdapter(adapter);
        workoutsRecView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    //#Set workout data
    public void setFragmentContent(){
        workouts = dataBaseHelper.getAllWorkoutsWithStatus(Enums.WorkoutStatus.WAITING.toString());
        Collections.sort(workouts,new WorkoutSortComparator());
        adapter.setWorkouts(workouts);
        if(workouts.isEmpty()){
            workoutsText.setText(R.string.no_workouts);
            workoutsText.setVisibility(View.VISIBLE);
        }
        else {
            workoutsText.setText(R.string.your_workouts);
            workoutsText.setVisibility(View.GONE);
        }
    }
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