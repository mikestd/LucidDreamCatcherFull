package com.mikestudio.luciddreamcatcher;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

/**
 * Вспомогательный класс для анимации
 */
public class AnimatorHelper {
    public static void AnimRotate_plus(TextView view){
        Animation anim = new RotateAnimation(0,360, view.getWidth()/2, view.getHeight()/2);
        anim.setDuration(400);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(anim);
    }
    public static void AnimRotate_minus(TextView view){
        Animation anim = new RotateAnimation(360,0, view.getWidth()/2, view.getHeight()/2);
        anim.setDuration(400);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(anim);
    }
}
