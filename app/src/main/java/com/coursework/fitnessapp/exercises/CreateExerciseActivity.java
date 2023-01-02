package com.coursework.fitnessapp.exercises;

import static java.nio.file.Files.delete;
import static java.nio.file.Files.readAllBytes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.MainActivity;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.Image;
import com.coursework.fitnessapp.models.InternalStoragePhoto;
import com.coursework.fitnessapp.supportclasses.DurationFieldValidator;
import com.coursework.fitnessapp.supportclasses.InputFilterMinMax;
import com.coursework.fitnessapp.supportclasses.TimeDuration;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class CreateExerciseActivity extends AppCompatActivity {

    private TextInputLayout exerciseNameLayout;
    private TextInputLayout exerciseDescriptionLayout;
    private TextInputLayout exerciseDurationLayout;
    private TextInputLayout exerciseCountLayout;
    private RelativeLayout imagesLayout;

    private EditText exerciseName;
    private EditText exerciseDescription;
    private EditText exerciseCount;
    private ImageView exercisePreviewImg;

    private EditText txtHours;
    private EditText txtMinutes;
    private EditText txtSeconds;
    private ImageButton hoursArrowUp;
    private ImageButton hoursArrowDown;
    private ImageButton minutesArrowUp;
    private ImageButton minutesArrowDown;
    private ImageButton secondsArrowUp;
    private ImageButton secondsArrowDown;
    private RecyclerView exerciseImagesRecView;

    private Button addImagesBtn;
    private Button addExerciseBtn;

    Boolean isPreviewImageSet = false;
    ExerciseModel exercise;
    Image previewImage;
    DataBaseHelper dataBaseHelper;
    Boolean isEditMode = false;
    ImagesRecViewAdapter adapter = new ImagesRecViewAdapter();
    ArrayList<Image> images = new ArrayList<>();

    private static final int Read_Permission = 101;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        dataBaseHelper = new DataBaseHelper(CreateExerciseActivity.this);

        initLayout();
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            isEditMode = true;
            int id = Integer.parseInt(intent.getExtras().get("id").toString());
            exercise = dataBaseHelper.getExerciseById(id);
            setExerciseValues();
        }

        if(ContextCompat.checkSelfPermission(CreateExerciseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateExerciseActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }
    }
    private void initLayout(){
        exerciseName = findViewById(R.id.exerciseName);
        exerciseCount = findViewById(R.id.exerciseCount);
        exercisePreviewImg = findViewById(R.id.exercisePreviewImg);
        exerciseDescription = findViewById(R.id.exerciseDescription);
        exerciseImagesRecView = findViewById(R.id.exerciseImages);

        imagesLayout = findViewById(R.id.imagesLayout);
        exerciseNameLayout = findViewById(R.id.exerciseNameLayout);
        exerciseDescriptionLayout = findViewById(R.id.descriptionLayout);
        exerciseDurationLayout = findViewById(R.id.exerciseDurationLayout);
        exerciseCountLayout = findViewById(R.id.exerciseCountLayout);

        txtHours = findViewById(R.id.hours);
        txtMinutes = findViewById(R.id.minutes);
        txtSeconds = findViewById(R.id.seconds);
        txtHours.setFilters(new InputFilter[]{new InputFilterMinMax(0,59)});
        txtMinutes.setFilters(new InputFilter[]{new InputFilterMinMax(0,59)});
        txtSeconds.setFilters(new InputFilter[]{new InputFilterMinMax(0,59)});
        hoursArrowUp = findViewById(R.id.hoursArrowUp);
        hoursArrowDown = findViewById(R.id.hoursArrowDown);
        minutesArrowUp = findViewById(R.id.minutesArrowUp);
        minutesArrowDown = findViewById(R.id.minutesArrowDown);
        secondsArrowUp = findViewById(R.id.secondsArrowUp);
        secondsArrowDown = findViewById(R.id.secondsArrowDown);

        hoursArrowUp.setOnClickListener(changeTimeListener);
        hoursArrowDown.setOnClickListener(changeTimeListener);
        minutesArrowUp.setOnClickListener(changeTimeListener);
        minutesArrowDown.setOnClickListener(changeTimeListener);
        secondsArrowUp.setOnClickListener(changeTimeListener);
        secondsArrowDown.setOnClickListener(changeTimeListener);

        adapter.setImages(images);
        exerciseImagesRecView.setAdapter(adapter);
        exerciseImagesRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int positionDragged = dragged.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                Collections.swap(images,positionDragged,positionTarget);
                adapter.notifyItemMoved(positionDragged,positionTarget);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(exerciseImagesRecView);

        exercisePreviewImg.setOnClickListener(addPreviewImage);
        addImagesBtn = findViewById(R.id.addImagesBtn);
        addImagesBtn.setOnClickListener(addImages);
        addExerciseBtn = findViewById(R.id.addExerciseBtn);
        addExerciseBtn.setOnClickListener(addExercise);
    }
    View.OnClickListener addPreviewImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,getString(R.string.select_pictures)),2);
        }
    };
    View.OnClickListener addImages = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,getString(R.string.select_pictures)),1);
            //TODO:maybe change startActivity
        }
    };
    View.OnClickListener changeTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText text = txtHours;
            Boolean increase = false;
            switch(view.getId()){
                case R.id.hoursArrowUp:
                    text = txtHours;
                    increase = true;
                    break;
                case R.id.hoursArrowDown:
                    text = txtHours;
                    increase = false;
                    break;
                case R.id.minutesArrowUp:
                    text = txtMinutes;
                    increase = true;
                    break;
                case R.id.minutesArrowDown:
                    text = txtMinutes;
                    increase = false;
                    break;
                case R.id.secondsArrowUp:
                    text = txtSeconds;
                    increase = true;
                    break;
                case R.id.secondsArrowDown:
                    text = txtSeconds;
                    increase = false;
                    break;
            }
            int value;
            try{
                value = Integer.parseInt(String.valueOf(text.getText()));
            }
            catch (NumberFormatException e){
                value = 0;
            }
            if(increase && (value < 60)){
                value++;
            }
            else if(!increase && value > 0){
                value--;
            }
            String resultString = String.valueOf(value);
            if(resultString.length() < 2){
                resultString = "0" + resultString;
            }
            text.setText(resultString);
        }
    };
    View.OnClickListener addExercise = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            if(validateInput()){
                String name = exerciseName.getText().toString();
                String description = exerciseDescription.getText().toString();
                TimeDuration duration = new TimeDuration(txtHours.getText().toString(),txtMinutes.getText().toString(),txtSeconds.getText().toString());
                int count = Integer.parseInt(exerciseCount.getText().toString());
                if(previewImage == null){
                    previewImage = new Image(null,null);
                }else {
                    try {
                        previewImage.setName(previewImage.getName() + String.valueOf(Math.floor(Math.random() * (9*Math.pow(10,9))) + Math.pow(10,(9))));
                        InternalStoragePhoto.saveImageToInternalStorage(previewImage.getName(), MediaStore.Images.Media.getBitmap(CreateExerciseActivity.this.getContentResolver(),previewImage.getImage()),CreateExerciseActivity.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<String> imageNames = new ArrayList<>();
                images = adapter.getImages();
                for (Image imageToSave:images) {
                    imageToSave.setName(imageToSave.getName() + String.valueOf(Math.floor(Math.random() * (9*Math.pow(10,9))) + Math.pow(10,(9))));
                    try {
                        InternalStoragePhoto.saveImageToInternalStorage(imageToSave.getName(), MediaStore.Images.Media.getBitmap(CreateExerciseActivity.this.getContentResolver(),imageToSave.getImage()),CreateExerciseActivity.this);
                        imageNames.add(imageToSave.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ExerciseModel newExercise = new ExerciseModel(null,name,description,previewImage.getName(),imageNames,null,duration,count, Enums.ExerciseType.Custom.toString());
                if(isEditMode && !dataBaseHelper.getExerciseById(exercise.getId()).getType().equals(Enums.ExerciseType.Default.toString())){
                    newExercise.setId(exercise.getId());
                    dataBaseHelper.editExercise(newExercise);
                }else {
                    dataBaseHelper.addExercise(newExercise);
                }
                images = adapter.getImages();
                finish();
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setExerciseValues(){
        exerciseName.setText(exercise.getName());
        exerciseCount.setText(String.valueOf(exercise.getDefaultCount()));
        exerciseDescription.setText(exercise.getDescription());
        txtHours.setText(exercise.getDefaultLength().getHours());
        txtMinutes.setText(exercise.getDefaultLength().getMinutes());
        txtSeconds.setText(exercise.getDefaultLength().getSeconds());

        addExerciseBtn.setText(R.string.save_changes);
        System.out.println(exercise.getPreviewImageName() != null);
        if(exercise.getPreviewImageName() != null){
            exercisePreviewImg.setImageBitmap(InternalStoragePhoto.loadImageFromInternalStorage(CreateExerciseActivity.this,exercise.getPreviewImageName()).get(0).getBmp());
        }
        for (String exerciseImage: exercise.getImageNames()) {
            images.add(new Image(null,exerciseImage));
            adapter.setImages(images);
        }

    }
    private boolean validateInput(){
        boolean hasError = false;
        if(exerciseName.getText().toString().isEmpty()){
            exerciseNameLayout.setError(getResources().getString(R.string.empty_name_error));
            hasError = true;
        }else exerciseNameLayout.setError(null);
        if(exerciseDescription.getText().toString().isEmpty()){
            exerciseDescriptionLayout.setError(getResources().getString(R.string.empty_description_error));
            hasError = true;
        }else exerciseDescriptionLayout.setError(null);
        if(!DurationFieldValidator.validate(txtHours.getText().toString()) && !DurationFieldValidator.validate(txtMinutes.getText().toString()) && !DurationFieldValidator.validate(txtSeconds.getText().toString())){
            exerciseDurationLayout.setError(getResources().getString(R.string.zero_duration_error));
            hasError = true;
        }else exerciseDurationLayout.setError(null);
        if(!DurationFieldValidator.validate(exerciseCount.getText().toString())){
            exerciseCountLayout.setError(getResources().getString(R.string.zero_exercise_amount));
            hasError = true;
        }else exerciseCountLayout.setError(null);
        return !hasError;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            if(data.getClipData() != null){
                for(int i = 0;i < data.getClipData().getItemCount();i++){
                    images.add(new Image(data.getClipData().getItemAt(i).getUri(),getFileName(data.getClipData().getItemAt(i).getUri())));
                }

            }
            else if(data.getData() != null){
                String imageURL = data.getData().toString();
                images.add(new Image(Uri.parse(imageURL),getFileName(Uri.parse(imageURL))));
            }
            adapter.setImages(images);
        }else if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            if(data.getData() != null){
                String imageURL = data.getData().toString();
                if(exercise != null && exercise.getPreviewImageName() != null){
                    InternalStoragePhoto.deleteImageFromInternalStorage(exercise.getPreviewImageName(),CreateExerciseActivity.this);
                }
                exercisePreviewImg.setImageURI(Uri.parse(imageURL));
                previewImage = new Image(Uri.parse(imageURL),getFileName(Uri.parse(imageURL)));
            }
        }

    }

    private String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            if(result != null){
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }
        return result;
    }

}