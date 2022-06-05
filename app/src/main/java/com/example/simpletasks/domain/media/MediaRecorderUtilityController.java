package com.example.simpletasks.domain.media;

import android.media.MediaRecorder;

import java.io.File;

public class MediaRecorderUtilityController implements MediaRecorderUtility{
    @Override
    public MediaRecorder getAudioRecorder(File outputFile){
        String outputFileUri = outputFile.getAbsolutePath();

        MediaRecorder audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        audioRecorder.setOutputFile(outputFileUri);

        return audioRecorder;
    }

    @Override
    public MediaRecorder getVideoRecorder(File outputFile){
        String outputFileUri = outputFile.getAbsolutePath();

        MediaRecorder videoRecorder = new MediaRecorder();
        videoRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        videoRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        videoRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        videoRecorder.setOutputFile(outputFileUri);

        return videoRecorder;
    }
}
