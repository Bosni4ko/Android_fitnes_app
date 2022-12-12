package com.coursework.fitnessapp.exercises;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.InternalStoragePhoto;

import java.util.ArrayList;

import javax.xml.namespace.QName;

public class ImagesRecViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<InternalStoragePhoto> images;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        Button removeImgBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            removeImgBtn = itemView.findViewById(R.id.removeImgBtn);
        }
    }
}
