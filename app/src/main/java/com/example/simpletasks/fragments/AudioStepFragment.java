package com.example.simpletasks.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.simpletasks.R;

/**
 * Audio Step Fragment that displays a title image and the player for the audio
 * Use the {@link AudioStepFragment#getNewInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioStepFragment extends Fragment {
    // the fragment initialization parameters
    private static final String STEP_TITLE = "STEP_TITLE";
    private static final String IMAGE_PATH = "IMAGE_PATH";
    private static final String AUDIO_PATH = "AUDIO_PATH";

    private final String TAG = "AudioStepFragment";

    // the parameters to initialize
    private String stepTitle;
    private String imagePath;
    private String audioPath;

    // UI fields
    private TextView title;
    private ImageView imageView;


    public AudioStepFragment() {
        // Required empty public constructor
    }

    /**
     * Get an instance of the audio step fragment.
     *
     * @param stepTitle the title of the audio step
     * @param imagePath the path to the steps title image
     * @param audioPath the path to the steps audio
     * @return instance of fragment AudioStepFragment
     */
    public static AudioStepFragment getNewInstance(String stepTitle, String imagePath, String audioPath) {
        AudioStepFragment fragment = new AudioStepFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STEP_TITLE, stepTitle);
        bundle.putString(IMAGE_PATH, imagePath);
        bundle.putString(AUDIO_PATH, audioPath);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stepTitle = getArguments().getString(STEP_TITLE);
            imagePath = getArguments().getString(IMAGE_PATH);
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
        View view = inflater.inflate(R.layout.fragment_show_audio_step, container, false);
        initializeFields(view);
        initializeUi();

        return view;
    }

    // Initialize the fields of the audio step fragment
    private void initializeFields(View view){
        title = view.findViewById(R.id.tv_showaudiostep_title);
        imageView = view.findViewById(R.id.iv_showaudiostep_image);
        Log.d(TAG, "Fragment fields initialized.");
    }

    // Initialize the state of the audio step fragment
    private void initializeUi(){
        title.setText(stepTitle);

        if(imagePath != null && !imagePath.isEmpty()){
            imageView.setImageURI(Uri.parse(imagePath));
        } else {
            imageView.setImageResource(R.drawable.image_placeholder);
        }

        Log.d(TAG, "Fragment state initialized.");
        setAudioPlayer();
    }

    // Set the audio player fragment
    private void setAudioPlayer(){
        Log.d(TAG, "Audio player set.");
        getChildFragmentManager().beginTransaction()
                .add(R.id.frag_showaudiostep_audioplayer, AudioPlayerFragment.getNewInstance(audioPath)).commit();
    }
}