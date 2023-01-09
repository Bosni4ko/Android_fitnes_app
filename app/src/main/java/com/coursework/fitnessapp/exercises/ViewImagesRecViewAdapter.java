package com.coursework.fitnessapp.exercises;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.models.Image;
import com.coursework.fitnessapp.models.InternalStoragePhoto;

import java.util.ArrayList;

//#ImagesRecViewAdapter is responsible for showing list of images in exercise detailed view
public class ViewImagesRecViewAdapter extends RecyclerView.Adapter<ViewImagesRecViewAdapter.ViewHolder>{

    private ArrayList<String> images;
    private Context context;
    @NonNull
    @Override
    public ViewImagesRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_view_image_list_item,parent,false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewImagesRecViewAdapter.ViewHolder holder, int position) {
        String image = images.get(position);
        ArrayList<InternalStoragePhoto> loadedImage = InternalStoragePhoto.loadImageFromInternalStorage(context,image);
        if(!loadedImage.isEmpty()){
            holder.image.setImageBitmap(loadedImage.get(0).getBmp());
        }
    }

    @Override
    public int getItemCount() {
        if(images != null){
            return images.size();
        }else return 0;
    }

    public void setImages(ArrayList<String> images){
        this.images = images;
        notifyDataSetChanged();
    }
    public ArrayList<String> getImages(){
        return images;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
