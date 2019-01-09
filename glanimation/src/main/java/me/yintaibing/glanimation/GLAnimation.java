package me.yintaibing.glanimation;

import android.opengl.Matrix;
import android.support.annotation.CallSuper;
import android.util.Log;

public abstract class GLAnimation {
    private static final String TAG = "GLAnimation";

    public static final int RELATIVE_TO_SELF = 0;
    public static final int RELATIVE_TO_PARENT = 1;

    protected float[] mMatrix = new float[16];
    //    protected int mParentWidth;
//    protected int mParentHeight;
//    protected int mChildWidth;
//    protected int mChildHeight;
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

    @CallSuper
    public void prepare(int parentWidth, int parentHeight, GLView view) {
        resetIdentity();
    }

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

    /**
     * 计算动画的尺寸
     *
     * @param type       尺寸类型。{@link #RELATIVE_TO_PARENT} and {@link #RELATIVE_TO_SELF}
     * @param value      尺寸值
     * @param size       展示动画的GLView的相应尺寸
     * @param parentSize 父SurfaceView的相应尺寸
     * @return 动画尺寸
     */
    protected float resolveSize(int type, float value, int size, int parentSize) {
        switch (type) {
            case RELATIVE_TO_PARENT:
//                return Utils.toWorldCoord(parentSize * value - base, parentSize, originated);
                return parentSize * value;

            case RELATIVE_TO_SELF:
            default:
//                return Utils.toWorldCoord(size * value, parentSize, originated);
                return size * value;
        }
    }

    protected boolean isRelativeToParent(int type) {
        return type == RELATIVE_TO_PARENT;
    }
}
