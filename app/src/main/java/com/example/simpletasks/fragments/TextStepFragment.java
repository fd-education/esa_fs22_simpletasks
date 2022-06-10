package com.example.simpletasks.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simpletasks.R;

/**
 * Fragment for the text task guide screens.
 */
public class TextStepFragment extends Fragment {
    // the fragment initialization parameters
    private static final String STEP_TITLE = "STEP_TITLE";
    private static final String IMAGE_PATH = "IMAGE_PATH";
    private static final String STEP_DESCRIPTION = "STEP_DESCRIPTION";

    private static final String TAG = "TextStepFragment";

    // the parameters to initialize
    private String stepTitle;
    private String imagePath;
    private String stepDescription;

    // UI fields
    private TextView title;
    private ImageView imageView;
    private TextView description;

    public TextStepFragment(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stepTitle Parameter 1.
     * @param imagePath Parameter 2.
     * @param stepDescription Parameter 3.
     * @return A new instance of fragment TaskStepFragment.
     */
    public static TextStepFragment getNewInstance(String stepTitle, String imagePath, String stepDescription) {
        TextStepFragment fragment = new TextStepFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STEP_TITLE, stepTitle);
        bundle.putString(IMAGE_PATH, imagePath);
        bundle.putString(STEP_DESCRIPTION, stepDescription);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            stepTitle = getArguments().getString(STEP_TITLE);
            imagePath = getArguments().getString(IMAGE_PATH);
            stepDescription = getArguments().getString(STEP_DESCRIPTION);
        }
    }

    /**
     * Inflate the fragments layout and set the adapter for the task steps list.
     *
     * @param inflater layout inflater to inflate the views in the fragment
     * @param container parent view of the fragments ui
     * @param savedInstanceState reconstruction of a previous state
     * @return View for the fragments ui
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_text_step, container, false);

        initializeFields(view);
        initializeUi();

        Log.d(TAG, "finished initialisation");
        return view;
    }

    // Initialize the fields of the text step fragment
    private void initializeFields(View view){
        title = view.findViewById(R.id.tv_showtextstep_title);
        imageView = view.findViewById(R.id.iv_showtextstep_image);
        description = view.findViewById(R.id.tv_showtextstep_description);
    }

    // Initialize the state of the text step fragment
    private void initializeUi(){
        title.setText(stepTitle);

        if(imagePath != null && !imagePath.isEmpty()){
            imageView.setImageURI(Uri.parse(imagePath));
        } else {
            imageView.setImageResource(R.drawable.image_placeholder);
        }

        description.setText(stepDescription);
    }
}