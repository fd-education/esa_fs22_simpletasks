package com.example.simpletasks.domain.ui;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Utility class that contains animations for the SimpleTasks UI
 */
public class AnimationUtils {

    /**
     * Get an animation that makes the desired view blink by setting its alpha value to 0 or 1 respectively.
     * Values between 0 and 1 are interpolated.
     *
     * @return animation
     */
    public static Animation getBlinkingAnimation(){
        int durationInMillis = 750;
        int fullyVisible = 1;
        int transparent = 0;

        Animation animation = new AlphaAnimation(fullyVisible, transparent);
        animation.setDuration(durationInMillis);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        return animation;
    }
}
