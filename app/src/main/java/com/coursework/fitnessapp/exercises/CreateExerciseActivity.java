package com.coursework.fitnessapp.exercises;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.coursework.fitnessapp.DataBaseHelper.DataBaseHelper;
import com.coursework.fitnessapp.R;
import com.coursework.fitnessapp.enums.Enums;
import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.models.Image;
import com.coursework.fitnessapp.models.InternalStoragePhoto;
import com.coursework.fitnessapp.supportclasses.DurationFieldValidator;
import com.coursework.fitnessapp.supportclasses.InputFilterMinMax;
import com.coursework.fitnessapp.supportclasses.TimeDuration;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

//#Activity for creating or editing exercise
public class CreateExerciseActivity extends AppCompatActivity {

    private TextInputLayout exerciseNameLayout;
    private TextInputLayout exerciseDescriptionLayout;
    private TextInputLayout exerciseDurationLayout;
    private TextInputLayout exerciseCountLayout;

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
        //#Change mode to editing if there are an intent,then get exercise id and sets data
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            isEditMode = true;
            int id = Integer.parseInt(intent.getExtras().get("id").toString());
            exercise = dataBaseHelper.getExerciseById(id);
            setExerciseValues();
        }

        //#Check app permission to access external storage and request this permission if there are no any
        if(ContextCompat.checkSelfPermission(CreateExerciseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateExerciseActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }

    }
    //#Initialise layout
    private void initLayout(){
        exerciseName = findViewById(R.id.exerciseName);
        exerciseCount = findViewById(R.id.exerciseCount);
        exercisePreviewImg = findViewById(R.id.exercisePreviewImg);
        exerciseDescription = findViewById(R.id.exerciseDescription);
        exerciseImagesRecView = findViewById(R.id.exerciseImages);

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

        //#Allow user to change image positions by holding and moving it
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

    //#Listener to add an image as preview image from external storage
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

    //#Listener to add images from external storage
    View.OnClickListener addImages = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,getString(R.string.select_pictures)),1);
        }
    };

    //#Listener to change time field value
    View.OnClickListener changeTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText text = txtHours;
            boolean increase = false;
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
            text.setText(reformatTimeValue(resultString));
        }
    };

    //#Format time field value to 2 digit format
    private String reformatTimeValue(String timeString){
        if(timeString.length() < 2){
            timeString = "0" + timeString;
        }
        return timeString;
    }

    //#Format all time field values to 2 digit format
    private void reformatTimeValues(){
        txtHours.setText(reformatTimeValue(txtHours.getText().toString()));
        txtMinutes.setText(reformatTimeValue(txtMinutes.getText().toString()));
        txtSeconds.setText(reformatTimeValue(txtSeconds.getText().toString()));
    }

    //#Listener for add exercise button
    View.OnClickListener addExercise = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            if(validateInput()){
                String name = exerciseName.getText().toString();
                String description = exerciseDescription.getText().toString();
                reformatTimeValues();
                TimeDuration duration = new TimeDuration(txtHours.getText().toString(),txtMinutes.getText().toString(),txtSeconds.getText().toString());
                int count = Integer.parseInt(exerciseCount.getText().toString());
                if(previewImage == null){
                    previewImage = new Image(null,null);
                }else {
                    //save preview image to the internal storage
                    try {
                        //generate unique image name for image
                        previewImage.setName(previewImage.getName() + Math.floor(Math.random() * (9*Math.pow(10,9))) + Math.pow(10,(9)));
                        InternalStoragePhoto.saveImageToInternalStorage(previewImage.getName(), MediaStore.Images.Media.getBitmap(CreateExerciseActivity.this.getContentResolver(),previewImage.getImage()),CreateExerciseActivity.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<String> imageNames = new ArrayList<>();
                images = adapter.getImages();
                for (Image imageToSave:images) {
                    //generate unique image names for images
                    imageToSave.setName(imageToSave.getName() + Math.floor(Math.random() * (9*Math.pow(10,9))) + Math.pow(10,(9)));
                    //save images to the internal storage
                    try {
                        InternalStoragePhoto.saveImageToInternalStorage(imageToSave.getName(), MediaStore.Images.Media.getBitmap(CreateExerciseActivity.this.getContentResolver(),imageToSave.getImage()),CreateExerciseActivity.this);
                        imageNames.add(imageToSave.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //creates a new exercise model and save it to the DB if not in edit mode and edit existed exercise if in edit mode
                ExerciseModel newExercise = new ExerciseModel(null,name,description,previewImage.getName(),imageNames,duration,count, Enums.ExerciseType.Custom.toString());
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

    //#Set values of exercise
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
    //#Validate exercise input fields
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

    //#Handle images or preview image import from external storage
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //#If images was added
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            if(data != null && data.getClipData() != null){
                for(int i = 0;i < data.getClipData().getItemCount();i++){
                    images.add(new Image(data.getClipData().getItemAt(i).getUri(),getFileName(data.getClipData().getItemAt(i).getUri())));
                }

            }
            else if(data != null && data.getData() != null){
                String imageURL = data.getData().toString();
                images.add(new Image(Uri.parse(imageURL),getFileName(Uri.parse(imageURL))));
            }
            adapter.setImages(images);
        }
        //#If preview image was added
        else if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            if(data != null && data.getData() != null){
                String imageURL = data.getData().toString();
                //#Delete current preview image if it exists
                if(exercise != null && exercise.getPreviewImageName() != null){
                    InternalStoragePhoto.deleteImageFromInternalStorage(exercise.getPreviewImageName(),CreateExerciseActivity.this);
                }
                exercisePreviewImg.setImageURI(Uri.parse(imageURL));
                previewImage = new Image(Uri.parse(imageURL),getFileName(Uri.parse(imageURL)));
            }
        }
    }

    //#Get image file name from image uri
    private String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
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