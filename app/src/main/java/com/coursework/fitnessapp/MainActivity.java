package com.coursework.fitnessapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;


import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.coursework.fitnessapp.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FloatingActionButton addFab;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        //dataBaseHelper.fillDataBase();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        setAddFabOnClickListener();
//        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
//
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                System.out.println("Item id is: " + item.getItemId());
//                System.out.println(R.id.navigation_home);
//                switch (item.getItemId()){
//                    case R.id.navigation_dashboard:
//                        setAddFabOnClickListener();
//                        break;
//
//                }
//                return true;
//            }
//        } );


    }

    private void setAddFabOnClickListener(){
        addFab = findViewById(R.id.addFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,CreateWorkoutActivity.class);
                startActivity(intent);
            }
        });
    }



}