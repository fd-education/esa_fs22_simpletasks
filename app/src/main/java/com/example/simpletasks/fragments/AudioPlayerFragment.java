package com.example.simpletasks.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.example.simpletasks.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * AudioPlayerFragment contains control buttons and a seekbar to control audio play.
 * Use the {@link AudioPlayerFragment#getNewInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioPlayerFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String AUDIO_PATH = "AUDIO_PATH";

    private final String TAG = "AudioStepFragment";

    // the parameters to initialize
    private String audioPath;

    // UI fields
    private SeekBar seekbar;
    private FloatingActionButton fabPlay;
    private FloatingActionButton fabPause;
    private FloatingActionButton fabStop;

    private MediaPlayer mediaPlayer;


    public AudioPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Get an instance of the audio player fragment.
     *
     * @param audioPath the path to the file containing the audio to play.
     * @return instance of fragment AudioStepFragment.
     */
    public static AudioPlayerFragment getNewInstance(String audioPath) {
        AudioPlayerFragment fragment = new AudioPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AUDIO_PATH, audioPath);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            audioPath = getArguments().getString(AUDIO_PATH);
        }
    }

    /**
     * Inflate the fragments layout in the containing view group
     * and setup the controls
     *
     * @param inflater for the layout
     * @param container of the layout
     * @param savedInstanceState state of the instance if saved
     * @return view containing the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_player, container, false);
        initializeFields(view);
        initializeUi();

        return view;
    }

    // Initialize the fields of the audio player
    private void initializeFields(View view){
        seekbar = view.findViewById(R.id.sb_audioplayer_progress);
        fabPlay = view.findViewById(R.id.fab_audioplayer_play);
        fabPause = view.findViewById(R.id.fab_audioplayer_pause);
        fabStop = view.findViewById(R.id.fab_audioplayer_stop);
        Log.d(TAG, "Fragment fields initialized.");
    }

    // Initialize the state of the audio player
    private void initializeUi(){
        if(audioPath != null && !audioPath.isEmpty()){
            controlAudio();
        } else {
            fabPlay.setEnabled(false);
            fabPause.setEnabled(false);
            fabStop.setEnabled(false);
        }

        Log.d(TAG, "Fragment state initialized.");
    }

    // Set the click listeners for all audio control components
    private void controlAudio(){
        Log.d(TAG, "Setting audio controls.");

        fabPlay.setOnClickListener(view -> {
            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(audioPath));

                initializeSeekbar();
            }

            mediaPlayer.start();
        });

        fabPause.setOnClickListener(view -> {
            if(mediaPlayer != null){
                mediaPlayer.pause();
            }
        });

        fabStop.setOnClickListener(view -> {
            if(mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    // Setup the seekbar and its progress
    private void initializeSeekbar(){
        seekbar.setMax(mediaPlayer.getDuration());

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                } catch(NullPointerException nex){
                    seekbar.setProgress(0);
                }
            }
        }, 0);
    }
}