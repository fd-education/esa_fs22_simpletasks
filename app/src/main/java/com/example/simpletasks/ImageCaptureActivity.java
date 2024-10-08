package com.example.simpletasks;

import static android.view.Surface.ROTATION_0;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.simpletasks.domain.fileSystem.FileSystemConstants;
import com.example.simpletasks.domain.fileSystem.FileSystemUtils;
import com.example.simpletasks.domain.fileSystem.FileSystemUtilsController;
import com.example.simpletasks.domain.ui.ButtonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * Activity for capturing images.
 */
public class ImageCaptureActivity extends AppCompatActivity {
    public static final String RESULT_KEY = "IMAGE_CAPTURE_RESULT";

    private final String TAG = "ImageCaptureActivity";
    private final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private File photoFile;
    private FileSystemUtils fsUtils;

    private ImageButton backButton;
    private FloatingActionButton fabCaptureImage;
    private FloatingActionButton fabPickImage;
    private FloatingActionButton fabRedo;
    private Button saveImage;

    private PreviewView previewView;
    private ImageView showImage;
    private ImageCapture imageCapture;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ActivityResultLauncher<Intent> cropImage;
    private ActivityResultLauncher<Intent> pickFromGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);

        initializeFields();
        initializeUi();
        requestPermissions();

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    // Initialize the fields of the image capture activity
    private void initializeFields() {
        fsUtils = new FileSystemUtilsController();
        showImage = findViewById(R.id.iv_imagecapture_show);
        previewView = findViewById(R.id.vv_imagecapture_preview);
        backButton = findViewById(R.id.ib_imagecapture_back_button);
        fabRedo = findViewById(R.id.fab_imagecapture_redo);
        fabCaptureImage = findViewById(R.id.fab_imagecapture_capture);
        fabPickImage = findViewById(R.id.fab_imagecapture_gallery);
        saveImage = findViewById(R.id.b_imagecapture_save);

        // Listener for the gallery pick result
        pickFromGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();

                    if (result.getResultCode() == RESULT_OK && data != null) {
                        File newPhotoFile;
                        try {
                            try {
                                newPhotoFile = fsUtils.createImageFile(getExternalFilesDir(FileSystemConstants.IMAGE_DIR));
                            } catch (IOException e) {
                                Log.e(TAG, e.toString());
                                String message = "Could not create image file.";
                                Toast.makeText(ImageCaptureActivity.this, message, Toast.LENGTH_LONG).show();
                                return;
                            }

                            InputStream inputStream = getContentResolver().openInputStream(data.getData());
                            FileOutputStream fileOutputStream = new FileOutputStream(newPhotoFile);
                            fsUtils.copyStream(inputStream, fileOutputStream);
                            fileOutputStream.close();
                            inputStream.close();
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                            return;
                        }

                        photoFile = newPhotoFile;
                        performCrop();
                    }
                });

        // Listener for the crop image result
        cropImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        showImage();
                    } else {
                        Toast.makeText(ImageCaptureActivity.this, "Image must be rectangular", Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Initialize the state of the image capture activity
    private void initializeUi() {
        ButtonUtils.disableButton(saveImage);
        showImage.setVisibility(View.GONE);
        previewView.setVisibility(View.VISIBLE);

        backButton.setOnClickListener(view -> handleBackPressed());

        fabRedo.setOnClickListener(view -> redoImageCapture());
        fabCaptureImage.setOnClickListener(view -> capturePhoto());
        fabPickImage.setOnClickListener(view -> pickImageFromGallery());

        saveImage.setOnClickListener(view -> setResult());
    }

    // Handle the back click and remove the file with the captured image
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handleBackPressed() {
        if (photoFile != null && photoFile.exists()) {
            photoFile.delete();
        }

        super.onBackPressed();
    }

    // Handle the ui changes if the user wants to redo the image
    private void redoImageCapture() {
        showImage.setVisibility(View.GONE);
        previewView.setVisibility(View.VISIBLE);
        fabRedo.setVisibility(View.GONE);
        ButtonUtils.enableImageButton(fabCaptureImage);
        ButtonUtils.disableButton(saveImage);
    }

    // Launch intent to pick an image from the gallery
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickFromGallery.launch(intent);
    }

    // Launch intent to crop the image picked from the gallery
    private void performCrop() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent
                    .setDataAndType(FileProvider.getUriForFile(this, FileSystemConstants.FILEPROVIDER_AUTHORITY, photoFile), "image/*")
                    .putExtra("crop", "true")
                    .putExtra("aspectX", 1).putExtra("aspectY", 1)
                    .putExtra("return-data", true)
                    .putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, FileSystemConstants.FILEPROVIDER_AUTHORITY, photoFile))
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            cropImage.launch(cropIntent);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    // Request the required permissions to take an image
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 1);
    }

    // Get the executor for the current activity
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    // Start and configure cameraX
    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        // Unbind all previously bound use cases
        cameraProvider.unbindAll();

        // Require the back facing camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Build the preview use case and bind it to the preview view in the layout
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Build the video capture use case with a frame rate of 30 fps
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(ROTATION_0)
                .build();

        // Create viewport for the desired rectangular photo
        ViewPort viewPort = new ViewPort.Builder(
                new Rational(previewView.getWidth(), previewView.getHeight()), ROTATION_0)
                .build();

        // Create use case group to set the viewport for a cropped image
        UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                .addUseCase(preview)
                .addUseCase(imageCapture)
                .setViewPort(viewPort)
                .build();

        // Bind the camera to the lifecycle and set the use cases
        cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup);
    }

    // Capture the photo
    private void capturePhoto() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Inform the user about required permissions
            ButtonUtils.disableImageButton(fabCaptureImage);
            String message = "Permission for the camera must be granted in order to capture a photo.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }

        // If image capture was initialized successfully ...
        if (imageCapture != null) {
            // ... create the file to store the image
            try {
                photoFile = fsUtils.createImageFile(getExternalFilesDir(FileSystemConstants.IMAGE_DIR));
            } catch (IOException e) {
                String message = "Could not create photo file.";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Log.e(TAG, e.toString());
                return;
            }

            // Start the capturing and handle callbacks
            imageCapture.takePicture(
                    new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                    getExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            Log.d(TAG, "Photo successfully saved. Uri: " + outputFileResults.getSavedUri());
                            showImage();
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            String message = "Photo could not be saved!";
                            Toast.makeText(ImageCaptureActivity.this, message, Toast.LENGTH_LONG).show();
                            Log.e(TAG, exception.getMessage());
                        }
                    }
            );
        }
    }

    // Show the captured image to the user and allow controls to redo the image
    private void showImage() {
        if (photoFile == null) {
            Log.e(TAG, "No image to load. Return from showImage()");
            return;
        }
        Log.d(TAG, "Showing photo: " + photoFile.getAbsolutePath());

        previewView.setVisibility(View.INVISIBLE);
        showImage.setVisibility(View.VISIBLE);
        fabRedo.setVisibility(View.VISIBLE);

        ButtonUtils.disableImageButton(fabCaptureImage);
        ButtonUtils.enableButton(saveImage);

        showImage.setImageURI(Uri.fromFile(photoFile));
    }

    // Set the result of the activity
    private void setResult() {
        Intent result = new Intent();

        if (photoFile != null && photoFile.exists()) {
            result.putExtra(RESULT_KEY, Uri.fromFile(photoFile));
            setResult(RESULT_OK, result);
        } else {
            result.putExtra(RESULT_KEY, "");
            setResult(RESULT_CANCELED, result);
        }

        finish();
    }
}