package com.example.simpletasks.domain.ui;

import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ButtonUtils {
    public static void disableFABs(List<FloatingActionButton> buttons){
        for(FloatingActionButton v: buttons){
            disableFAB(v);
        }
    }

    public static void disableFAB(FloatingActionButton button){
        button.setEnabled(false);
        button.setAlpha(.5f);
    }

    public static void disableButtons(List<Button> buttons){
        for(Button v: buttons){
            disableButton(v);
        }
    }

    public static void disableButton(Button button){
        button.setEnabled(false);
        button.setAlpha(.5f);
    }

    public static void disableImageButtons(List<ImageButton> buttons){
        for(ImageButton v: buttons){
            disableImageButton(v);
        }
    }

    public static void disableImageButton(ImageButton button){
        button.setEnabled(false);
        button.setAlpha(.5f);
    }

    public static void enableFABs(List<FloatingActionButton> buttons){
        for(FloatingActionButton v: buttons){
            enableFAB(v);
        }
    }

    public static void enableFAB(FloatingActionButton button){
        button.setEnabled(true);
        button.setAlpha(1f);
    }

    public static void enableButtons(List<Button> buttons){
        for(Button v: buttons){
            enableButton(v);
        }
    }

    public static void enableButton(Button button){
        button.setEnabled(true);
        button.setAlpha(1f);
    }

    public static void enableImageButtons(List<ImageButton> buttons){
        for(ImageButton v: buttons){
            enableImageButton(v);
        }
    }

    public static void enableImageButton(ImageButton button){
        button.setEnabled(true);
        button.setAlpha(1f);
    }
}
