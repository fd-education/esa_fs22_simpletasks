package com.example.simpletasks.domain.fileSystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileSystemUtility {
    File createImageFile(File storageDir) throws IOException;
    File createVideoFile(File storageDir) throws IOException;
    File createAudioFile(File storageDir) throws IOException;
    void copyStream(InputStream input, OutputStream output) throws IOException;
}