package com.example.simpletasks.domain.ui;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Utility class to enable and disable FABs, Buttons and ImageButtons
 */
public class ButtonUtils {

    /**
     * Disable a list of floating action buttons and make the state visible by setting their alpha value to .5
     * @param buttons list of FloatingActionButton objects to be disabled
     */
    public static void disableFABs(List<FloatingActionButton> buttons){
        for(FloatingActionButton v: buttons){
            disableFAB(v);
        }
    }

    /**
     * Disable floating action button and make the state visible by setting its alpha value to .5
     * @param button FloatingActionButton object to be disabled
     */
    public static void disableFAB(FloatingActionButton button){
        button.setEnabled(false);
        button.setAlpha(.5f);
    }

    /**
     * Disable a list of buttons and make the state visible by setting their alpha value to .5
     * @param buttons list of Button objects to be disabled
     */
    public static void disableButtons(List<Button> buttons){
        for(Button v: buttons){
            disableButton(v);
        }
    }

    /**
     * Disable button and make the state visible by setting its alpha value to .5
     * @param button Button object to be disabled
     */
    public static void disableButton(Button button){
        button.setEnabled(false);
        button.setAlpha(.5f);
    }

    /**
     * Disable a list of image buttons and make the state visible by setting their alpha value to .5
     * @param buttons list of ImageButton objects to be disabled
     */
    public static void disableImageButtons(List<ImageButton> buttons){
        for(ImageButton v: buttons){
            disableImageButton(v);
        }
    }

    /**
     * Disable image button and make the state visible by setting its alpha value to .5
     * @param button ImageButton object to be disabled
     */
    public static void disableImageButton(ImageButton button){
        button.setEnabled(false);
        button.setAlpha(.5f);
    }

    /**
     * Enable a list of floating action buttons and make the state visible by setting their alpha value to 1
     * @param buttons list of FloatingActionButton objects to be enabled
     */
    public static void enableFABs(List<FloatingActionButton> buttons){
        for(FloatingActionButton v: buttons){
            enableFAB(v);
        }
    }

    /**
     * Enable floating action button and make the state visible by setting its alpha value to 1
     * @param button ImageButton object to be enabled
     */
    public static void enableFAB(FloatingActionButton button){
        button.setEnabled(true);
        button.setAlpha(1f);
    }

    /**
     * Enable a list of buttons and make the state visible by setting their alpha value to 1
     * @param buttons list of Button objects to be disabled
     */
    public static void enableButtons(List<Button> buttons){
        for(Button v: buttons){
            enableButton(v);
        }
    }

    /**
     * Enable button and make the state visible by setting its alpha value to 1
     * @param button Button object to be enabled
     */
    public static void enableButton(Button button){
        button.setEnabled(true);
        button.setAlpha(1f);
    }

    /**
     * Enable a list of image buttons and make the state visible by setting their alpha value to 1
     * @param buttons list of ImageButton objects to be disabled
     */
    public static void enableImageButtons(List<ImageButton> buttons){
        for(ImageButton v: buttons){
            enableImageButton(v);
        }
    }

    /**
     * Enable image button and make the state visible by setting its alpha value to 1
     * @param button ImageButton object to be enabled
     */
    public static void enableImageButton(ImageButton button){
        button.setEnabled(true);
        button.setAlpha(1f);
    }
}
