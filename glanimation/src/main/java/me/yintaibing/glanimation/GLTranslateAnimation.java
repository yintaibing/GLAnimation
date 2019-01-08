package me.yintaibing.glanimation;

import android.opengl.Matrix;

public class GLTranslateAnimation extends GLAnimation {
    private static final String TAG = "GLTranslateAnimation";

    // 暂时都是RELATIVE_TO_SELF
    private int mFromXType;
    private int mFromYType;
    private int mToXType;
    private int mToYType;
    private float mFromXValue;
    private float mFromYValue;
    private float mToXValue;
    private float mToYValue;

    private float mFromXDelta;
    private float mFromYDelta;
    private float mToXDelta;
    private float mToYDelta;

    public GLTranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue,
                                int fromYType, float fromYValue, int toYType, float toYValue) {
        mFromXType = fromXType;
        mFromXValue = fromXValue;
        mToXType = toXType;
        mToXValue = toXValue;
        mFromYType = fromYType;
        mFromYValue = fromYValue;
        mToYType = toYType;
        mToYValue = toYValue;
    }

    @Override
    protected void prepare() {
        mFromXDelta = resolveSize(mFromXType, mFromXValue, mChildWidth, mParentWidth);
        mFromYDelta = resolveSize(mFromYType, mFromYValue, mChildHeight, mParentHeight);
        mToXDelta = resolveSize(mToXType, mToXValue, mChildWidth, mParentWidth);
        mToYDelta = resolveSize(mToYType, mToYValue, mChildHeight, mParentHeight);

        if (mFillBefore) {
            if (mFromXDelta != 0f || mFromYDelta != 0f) {
                translate(0f);
            }
        }
    }

    @Override
    protected void update(float progress) {
        resetIdentity();
        translate(progress);
    }

    private void translate(float progress) {
        float dx = mFromXDelta;
        float dy = mFromYDelta;
        if (mFromXDelta != mToXDelta) {
            dx = mFromXDelta + ((mToXDelta - mFromXDelta) * progress);
        }
        if (mFromYDelta != mToYDelta) {
            dy = mFromYDelta + ((mToYDelta - mFromYDelta) * progress);
        }
        float tx = (dx / mChildWidth) * (mChildWidth / (mParentWidth * 0.5f));
        float ty = (dy / mChildHeight) * (mChildHeight / (mParentHeight * 0.5f));
//        Log.e(TAG, "dx=" + dx
//                + " tx=" + tx
//                + " dy=" + dy
//                + " ty=" + ty);
        Matrix.translateM(mMatrix, 0, tx, ty, 0f);
    }
}
