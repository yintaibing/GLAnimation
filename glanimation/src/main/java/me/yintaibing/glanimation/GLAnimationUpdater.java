package me.yintaibing.glanimation;

import android.animation.ValueAnimator;
import android.util.Log;

public class GLAnimationUpdater {
    private GLView mView;
    private GLAnimation mAnimation;
    private ValueAnimator mValueAnimator;

    public GLAnimationUpdater(GLView view, GLAnimation animation) {
        this.mView = view;
        this.mAnimation = animation;
        this.mValueAnimator = ValueAnimator.ofFloat(0f, 1f)
                .setDuration(animation.getDuration());
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                mAnimation.updateProgress(progress);
            }
        });
    }

    public void prepare(int parentWidth, int parentHeight) {
        mAnimation.prepare(parentWidth, parentHeight, mView);
    }

    public void start() {
        Log.e("GLAnimationUpdater", "name=" + mView.mName + " start");
        mValueAnimator.start();
    }
}
