package com.coursework.fitnessapp.exercises;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.Image;
import com.coursework.fitnessapp.models.InternalStoragePhoto;

import java.util.ArrayList;

import javax.xml.namespace.QName;

public class ImagesRecViewAdapter extends RecyclerView.Adapter<ImagesRecViewAdapter.ViewHolder>{

    private ArrayList<Image> images;
    private Context context;
    @NonNull
    @Override
    public ImagesRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_image_list_item,parent,false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesRecViewAdapter.ViewHolder holder, int position) {
        Image image = images.get(position);
        holder.image.setImageURI(image.getImage().normalizeScheme());
        holder.removeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                images.remove(image);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setImages(ArrayList<Image> images){
        this.images = images;
        notifyDataSetChanged();
    }
    public ArrayList<Image> getImages(){
        return images;
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
