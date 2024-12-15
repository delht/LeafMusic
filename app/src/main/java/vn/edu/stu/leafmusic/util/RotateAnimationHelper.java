package vn.edu.stu.leafmusic.util;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.View;

public class RotateAnimationHelper {
    private RotateAnimation rotateAnimation;
    private boolean isPaused = false;
    private float currentDegree = 0f;

    public RotateAnimationHelper() {
        createAnimation();
    }

    private void createAnimation() {
        rotateAnimation = new RotateAnimation(
            currentDegree, currentDegree + 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(10000); // 10 giây cho một vòng quay
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setFillAfter(true);
    }

    public void startAnimation(View view) {
        if (isPaused) {
            createAnimation();
        }
        view.startAnimation(rotateAnimation);
        isPaused = false;
    }

    public void pauseAnimation(View view) {
        if (rotateAnimation != null) {
            currentDegree = currentDegree % 360 + 
                (rotateAnimation.getDuration() - 
                 rotateAnimation.getStartTime()) * 360f / rotateAnimation.getDuration();
            view.clearAnimation();
            isPaused = true;
        }
    }

    public void resumeAnimation(View view) {
        if (isPaused) {
            startAnimation(view);
        }
    }

    public void stopAnimation(View view) {
        if (rotateAnimation != null) {
            view.clearAnimation();
            currentDegree = 0f;
            isPaused = false;
        }
    }

    public boolean isPaused() {
        return isPaused;
    }
} 