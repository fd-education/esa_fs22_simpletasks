package com.example.simpletasks.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simpletasks.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoStepFragment#getNewInstance(String, String)} newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoStepFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STEP_TITLE = "STEP_TITLE";
    private static final String VIDEO_PATH = "VIDEO_PATH";

    private final String TAG = "VideoStepFragment";

    // the parameters to initialize
    private String stepTitle;
    private String videoPath;

    // UI fields
    private TextView title;

    private VideoStepFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stepTitle Parameter 1.
     * @param videoPath Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoStepFragment getNewInstance(String stepTitle, String videoPath) {
        VideoStepFragment fragment = new VideoStepFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STEP_TITLE, stepTitle);
        bundle.putString(VIDEO_PATH, videoPath);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stepTitle = getArguments().getString(STEP_TITLE);
            videoPath = getArguments().getString(VIDEO_PATH);
        }

        Log.d(TAG, "Video Step Fragment created for " + stepTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_video_step, container, false);
        initializeFields(view);
        initializeUi();

        return view;
    }

    // Initialize the fields of the video step fragment
    private void initializeFields(View view){
        title = view.findViewById(R.id.tv_showvideostep_title);
    }

    // Initialize the state of the video step fragment
    private void initializeUi(){
        title.setText(stepTitle);

        setVideoPlayer();
        Log.d(TAG, "ui initialized");
    }

    // Set the video player fragment
    private void setVideoPlayer(){
        getChildFragmentManager().beginTransaction()
                .add(R.id.frag_showvideostep_videoplayer, VideoPlayerFragment.getNewInstance(videoPath)).commit();
    }
}