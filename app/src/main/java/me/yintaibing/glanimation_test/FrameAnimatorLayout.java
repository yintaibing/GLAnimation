package me.yintaibing.glanimation_test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 帧动画控件父类
 *
 * @author yintaibing
 * @date 2017/12/13
 */

public abstract class FrameAnimatorLayout extends FrameLayout {
    protected static final int FRAME_DURATION = 100;

    private List<CancelableTask> mTasks;

    public FrameAnimatorLayout(Context context, AttributeSet attributeSet, int style) {
        super(context, attributeSet, style);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTasks != null) {
            for (CancelableTask task : mTasks) {
                task.cancel();
                removeCallbacks(task);
            }
            mTasks.clear();
        }
    }

    protected void postDelayedTask(CancelableTask task, long delayMillis) {
        postDelayed(task, delayMillis);

        if (mTasks == null) {
            mTasks = new ArrayList<>();
        }
        mTasks.add(task);
    }
}
