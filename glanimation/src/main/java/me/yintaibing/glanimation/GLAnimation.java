package me.yintaibing.glanimation;

import android.opengl.Matrix;
import android.util.Log;

public abstract class GLAnimation {
    private static final String TAG = "GLAnimation";

    public static final int RELATIVE_TO_PARENT = 0;
    public static final int RELATIVE_TO_SELF = 1;

    protected float[] mMatrix = new float[16];
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
        resetIdentity();
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

    protected void resetIdentity() {
        Matrix.setIdentityM(mMatrix, 0);
    }

    protected static float resolveSize(int type, float value, int size, int parentSize) {
        switch (type) {
            case RELATIVE_TO_SELF:
                return size * value;
            case RELATIVE_TO_PARENT:
            default:
                return parentSize * value;
        }
    }
}
