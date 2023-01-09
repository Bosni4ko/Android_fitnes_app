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

//#ImagesRecViewAdapter is responsible for showing list of images in exercise creation or editing view
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ImagesRecViewAdapter.ViewHolder holder, int position) {
        Image image = images.get(position);
        //#Load preview image if it exists
        if(image.getImage() != null){
            holder.image.setImageURI(image.getImage().normalizeScheme());
        }else {
            holder.image.setImageBitmap(InternalStoragePhoto.loadImageFromInternalStorage(context, image.getName()).get(0).getBmp());
        }
        //#Listener for removing added image from the list
        holder.removeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                images.remove(image);
                if(image.getImage() == null){
                    InternalStoragePhoto.deleteImageFromInternalStorage(image.getName(),context);
                }
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


    //#Initialise viewholder layout
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
