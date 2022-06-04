package com.example.simpletasks.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.example.simpletasks.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
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
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param audioPath Parameter 2.
     * @return A new instance of fragment AudioStepFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_player, container, false);
        initializeFields(view);
        initializeUi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initializeFields(View view){
        seekbar = view.findViewById(R.id.sb_audioplayer_progress);
        fabPlay = view.findViewById(R.id.fab_audioplayer_play);
        fabPause = view.findViewById(R.id.fab_audioplayer_pause);
        fabStop = view.findViewById(R.id.fab_audioplayer_stop);
    }

    private void initializeUi(){

        if(audioPath != null && !audioPath.isEmpty()){
            controlAudio();
        } else {
            fabPlay.setEnabled(false);
            fabPause.setEnabled(false);
            fabStop.setEnabled(false);
        }
    }

    private void controlAudio(){
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