package com.coursework.fitnessapp.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class InternalStoragePhoto {
    private String name;
    private Bitmap bmp;

    public InternalStoragePhoto(String name, Bitmap bmp) {
        this.name = name;
        this.bmp = bmp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public static Boolean saveImageToInternalStorage(String fileName, Bitmap bmp, Context context){
        try{
            FileOutputStream fos = context.openFileOutput(fileName + ".jpg",context.MODE_PRIVATE);
            if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95,fos)){
                throw new IOException("Couldn't save bitmap");
            }
            fos.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<InternalStoragePhoto> loadImageFromInternalStorage(Context context,String name){
        ArrayList<InternalStoragePhoto> internalImages = new ArrayList<>();
        File[] files = context.getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file,String name) {
                return(file.canRead() && file.isFile() && name.startsWith(name));
            }
        });
        Arrays.stream(files).map(file -> {
            try {
                Bitmap bmp = BitmapFactory.decodeByteArray(Files.readAllBytes(file.toPath()),0, Files.readAllBytes(file.toPath()).length);
                internalImages.add(new InternalStoragePhoto(file.getName(),bmp));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        return internalImages;
    }

    public boolean deleteImageFromInternalStorage(String fileName,Context context){
        return context.deleteFile(fileName);
    }
}
