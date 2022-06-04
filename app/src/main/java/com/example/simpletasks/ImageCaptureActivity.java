package com.example.simpletasks;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.simpletasks.domain.fileSystem.FileSystemConstants;
import com.example.simpletasks.domain.fileSystem.FileSystemUtility;
import com.example.simpletasks.domain.fileSystem.FileSystemUtilityController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageCaptureActivity extends AppCompatActivity {
    private final String TAG = "ImageCaptureActivity";

    private final Context context = this;

    private File photoFile;
    private FileSystemUtility fileSystemUtility;

    private ImageButton backButton;
    private ImageButton captureImage;
    private ImageButton pickImage;
    private Button saveImage;

    private ImageView titleImageView;
    private Uri imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);

        initializeFields();
        initializeUi();
        titleImageView.setImageResource(R.drawable.image_placeholder);

        handleIntent(getIntent());
    }

    private void initializeFields(){
        fileSystemUtility = new FileSystemUtilityController();
        titleImageView = findViewById(R.id.iv_imagecapture_preview);

        backButton = findViewById(R.id.ib_imagecapture_back_button);
        captureImage = findViewById(R.id.ib_imagecapture_capture);
        pickImage = findViewById(R.id.ib_imagecapture_gallery);
        saveImage = findViewById(R.id.b_imagecapture_save);
    }

    private void initializeUi(){
        backButton.setOnClickListener(view -> {
            Log.e(TAG, "Back button pressed.");
            ImageCaptureActivity.super.onBackPressed();
        });

        captureImage.setOnClickListener(view -> {
            Log.d(TAG, "Take image capture clicked.");
            captureImage();
        });

        pickImage.setOnClickListener(view -> {
            Log.d(TAG, "Pick image from gallery clicked.");
            pickImageFromGallery();
        });

        saveImage.setOnClickListener(view -> returnImage());
    }

    private void captureImage(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try{
                Log.d(TAG, "Creating photofile.");
                photoFile = fileSystemUtility.createImageFile(getExternalFilesDir(FileSystemConstants.IMAGE_DIR));
            } catch(IOException ex){
                // TODO
                Log.e(TAG, ex.toString());
            }

            if(photoFile != null){
                Log.d(TAG, "Launching intent.");
                Uri photoUri = FileProvider.getUriForFile(this, FileSystemConstants.FILEPROVIDER_AUTHORITY, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                takePicture.launch(takePictureIntent);
            }
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickFromGallery.launch(intent);
    }

    private void returnImage(){
        Intent data = new Intent();
        data.setData(Uri.fromFile(photoFile));
        setResult(RESULT_OK, data);
        finish();
    }

    final ActivityResultLauncher<Intent> takePicture = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        imagePath = Uri.fromFile(new File(photoFile.getAbsolutePath()));
                        performCrop();
                    }
                }
            });

    final ActivityResultLauncher<Intent> pickFromGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();

                    if (result.getResultCode() == RESULT_OK && data != null) {
                        File newPhotoFile;
                        try{
                            try{
                                newPhotoFile = fileSystemUtility.createImageFile(getExternalFilesDir(FileSystemConstants.IMAGE_DIR));
                            } catch(IOException e){
                                Log.e(TAG, e.toString());
                                return;
                                // TODO
                            }

                            InputStream inputStream = getContentResolver().openInputStream(data.getData());
                            FileOutputStream fileOutputStream = new FileOutputStream(newPhotoFile);
                            fileSystemUtility.copyStream(inputStream, fileOutputStream);
                            fileOutputStream.close();
                            inputStream.close();
                        } catch(Exception e){
                            Log.e(TAG, e.toString());
                            return;
                            // TODO
                        }

                        photoFile = newPhotoFile;
                        performCrop();
                    }
                }
            });

    private void performCrop(){
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(FileProvider.getUriForFile(this, FileSystemConstants.FILEPROVIDER_AUTHORITY, photoFile), "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1).putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256).putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, FileSystemConstants.FILEPROVIDER_AUTHORITY, photoFile));
            cropImage.launch(cropIntent);
        } catch(ActivityNotFoundException anfe){
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    final ActivityResultLauncher<Intent> cropImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    showImage();
                } else {
                    // TODO
                    Toast.makeText(context, "Image must be rectangular", Toast.LENGTH_LONG).show();
                }
            });

    private void showImage(){
        if(photoFile == null){
            Log.d(TAG, "No image to load. Return from showImage()");
            return;
        }
        Log.e(TAG, photoFile.getAbsolutePath());

        // Create new file object to get android uri from
        titleImageView.setImageURI(Uri.fromFile(new File(photoFile.getAbsolutePath())));
    }

    private void handleIntent(Intent intent){
        if(intent != null && intent.hasExtra("image_path")){
            String imageUri = intent.getStringExtra("image_path");
            Log.d(TAG, "" + imageUri);

            if(imageUri != null){
                imagePath = Uri.parse(imageUri);
                Log.e(TAG, imagePath.toString());
            } else{
                Log.e(TAG, "Set Placeholder for view.");
                titleImageView.setImageResource(R.drawable.image_placeholder);
            }
        }
    }
}