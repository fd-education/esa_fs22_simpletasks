package com.example.simpletasks.domain.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public class AnimationUtil {
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
