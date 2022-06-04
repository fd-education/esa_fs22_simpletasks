package com.example.simpletasks.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.example.simpletasks.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoPlayerFragment#getNewInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoPlayerFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String VIDEO_PATH = "VIDEO_PATH";

    private final String TAG = "VideoPlayerFragment";

    // the parameters to initialize
    private String videoPath;

    //UI fields
    private VideoView video;
    private FloatingActionButton fabPlay;
    private FloatingActionButton fabPause;
    private FloatingActionButton fabStop;


    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param videoPath Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoPlayerFragment getNewInstance(String videoPath) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_PATH, videoPath);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoPath = getArguments().getString(VIDEO_PATH);
        }

        Log.d(TAG, "Video Player Fragment created.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        initializeFields(view);
        initializeUi();

        return view;
    }

    private void initializeFields(View view) {
        video = view.findViewById(R.id.vv_videoplayer_video);
        fabPlay = view.findViewById(R.id.fab_videoplayer_play);
        fabPause = view.findViewById(R.id.fab_videoplayer_pause);
        fabPause.setVisibility(View.GONE);
        fabStop = view.findViewById(R.id.fab_videoplayer_stop);
    }

    private void initializeUi() {
        if (videoPath != null && !videoPath.isEmpty()) {
            setControlsEnabled(true);
            controlVideo();
            Log.d(TAG, "Video Controls enabled and initialized.");
        } else {
            setControlsEnabled(false);
            Log.d(TAG, "Video Controls disabled.");
        }
    }

    private void controlVideo() {
        video.setVideoPath(videoPath);
        // to make the first frame appear instead of a black view
        video.seekTo(1);

        fabPlay.setOnClickListener(view -> {
            video.start();
            fabPlay.setVisibility(View.GONE);
            fabPause.setVisibility(View.VISIBLE);
        });

        fabPause.setOnClickListener(view -> {
            if (video.canPause()) {
                video.pause();
                fabPause.setVisibility(View.GONE);
                fabPlay.setVisibility(View.VISIBLE);
            }
        });

        fabStop.setOnClickListener(view -> {
            if (video.canPause()) {
                video.pause();
                video.seekTo(0);
            }
        });

        video.setOnCompletionListener(mediaPlayer -> {
            video.seekTo(0);
            fabPause.setVisibility(View.GONE);
            fabPlay.setVisibility(View.VISIBLE);
        });
    }

    private void setControlsEnabled(Boolean enabled){
        fabPlay.setEnabled(enabled);
        fabPause.setEnabled(enabled);
        fabStop.setEnabled(enabled);

        if(enabled){
            setFabAlpha(1f);
        } else {
            setFabAlpha(.5f);
        }
    }

    private void setFabAlpha(float alpha){
        fabPlay.setAlpha(alpha);
        fabPause.setAlpha(alpha);
        fabStop.setAlpha(alpha);
    }
}