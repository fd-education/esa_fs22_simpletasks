package com.example.simpletasks.domain.media;

import android.media.MediaRecorder;

import java.io.File;

public interface MediaRecorderUtility {
    MediaRecorder getAudioRecorder(File outputFile);
    MediaRecorder getVideoRecorder(File outputFile);
}
