package com.example.simpletasks.domain.fileSystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileSystemUtility {
    public File createImageFile(File storageFile) throws IOException;
    public void copyStream(InputStream input, OutputStream output) throws IOException;
}