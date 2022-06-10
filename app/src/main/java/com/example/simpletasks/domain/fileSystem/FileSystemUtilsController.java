package com.example.simpletasks.domain.fileSystem;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSystemUtilsController implements FileSystemUtils {
    /**
     * Get the file object to store an image to.
     *
     * @param storageDir the directory to store the image file in.
     * @return the file to store the image to
     * @throws IOException if the file could not be created
     */
    @Override
    public File createImageFile(File storageDir) throws IOException {
        String fileName = "JPEG_" + getTimeStamp() + "_";

        return File.createTempFile(
                fileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    /**
     * Get the file object to store a video to.
     *
     * @param storageDir the directory to store the video file in.
     * @return the file to store the video to
     * @throws IOException if the file could not be created
     */
    @Override
    public File createVideoFile(File storageDir) throws IOException {
        String fileName = "MP4_" + getTimeStamp() + "_";

        return File.createTempFile(
                fileName,
                ".mp4",
                storageDir
        );
    }

    /**
     * Get the file object to store an audio recording to.
     *
     * @param storageDir the directory to store the audio file in.
     * @return the file to store the audio to
     * @throws IOException if the file could not be created
     */
    @Override
    public File createAudioFile(File storageDir) throws IOException {
        return createVideoFile(storageDir);
    }

    /**
     * Copy an input stream to a specified output
     *
     * @param input input stream
     * @param output output stream
     * @throws IOException if either source or target of the stream are invalid
     */
    @Override
    public void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    // Return a timestamp to act as a unique identifier for the file
    @SuppressLint("SimpleDateFormat")
    private String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

}
