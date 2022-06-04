package com.example.simpletasks.domain.fileSystem;

import com.example.simpletasks.BuildConfig;

public final class FileSystemConstants {
    private FileSystemConstants(){

    }

    // File names must correlate with the paths defined in file_paths.xml!!!
    public static final String IMAGE_DIR = "simpletasks_images";
    public static final String VIDEO_DIR = "simpletasks_videos";
    public static final String AUDIO_DIR = "simpletasks_recordings";

    // Authority must correlate with the file provider authority defined in android_manifest.xml
    public static final String FILEPROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";
}
