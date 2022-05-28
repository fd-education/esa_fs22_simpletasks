package com.example.simpletasks.domain.fileSystem;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSystemUtilityController implements FileSystemUtility{
    @Override
    public File createImageFile(File storageDir) throws IOException {
        String fileName = "JPEG_" + getTimeStamp() + "_";

        return File.createTempFile(
                fileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

     @Override
     public File createVideoFile(File storageDir) throws IOException{
        String fileName = "MP4_" + getTimeStamp() + "_";

         return File.createTempFile(
                 fileName,
                 ".mp4",
                 storageDir
         );
     }

    @Override
    public File createAudioFile(File storageDir) throws IOException{
        return createVideoFile(storageDir);
    }

    @Override
    public void copyStream(InputStream input, OutputStream output) throws IOException{
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
    }

    @SuppressLint("SimpleDateFormat")
    private String getTimeStamp(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

}
