package me.yintaibing.glanimation;

import android.opengl.Matrix;
import android.util.Log;

public abstract class GLAnimation {
    private static final String TAG = "GLAnimation";

    protected float[] mMatrix;
    protected int mParentWidth;
    protected int mParentHeight;
    protected int mChildWidth;
    protected int mChildHeight;
    protected long mDuration;
    protected long mStartOffset;
    protected boolean mFillBefore;
    protected boolean mFillAfter;
    protected boolean mFinished;

    public GLAnimation() {
        mMatrix = new float[16];
        Matrix.setIdentityM(mMatrix, 0);
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public long getDuration() {
        return mDuration;
    }

    public long getStartOffset() {
        return mStartOffset;
    }

    public void setStartOffset(long mStartOffset) {
        this.mStartOffset = mStartOffset;
    }

    public void setFillBefore(boolean fillBefore) {
        mFillBefore = fillBefore;
    }

    public void setFillAfter(boolean fillAfter) {
        mFillAfter = fillAfter;
    }

    public float[] getMatrix() {
        return mMatrix;
    }

    void prepare(int parentWith, int parentHeight, int childWidth, int childHeight) {
        mParentWidth = parentWith;
        mParentHeight = parentHeight;
        mChildWidth = childWidth;
        mChildHeight = childHeight;
        prepare();
    }

    protected abstract void prepare();

    void updateProgress(float progress) {
        if (progress >= 1f) {
            mFinished = true;
            Log.e(TAG, "animation finished");
        }
        update(progress);
    }

    protected abstract void update(float progress);
}
