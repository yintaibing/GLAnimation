package me.yintaibing.glanimation;

import android.opengl.Matrix;

public class GLRotateAnimation extends GLAnimation {
    private float mFromDegrees;
    private float mToDegrees;
    private int mPivotXType;
    private float mPivotXValue;
    private int mPivotYType;
    private float mPivotYValue;

    private float mPivotX;
    private float mPivotY;

    public GLRotateAnimation(float fromDegrees, float toDegrees,
                             int pivotXType, float pivotXValue,
                             int pivotYType, float pivotYValue) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mPivotXType = pivotXType;
        mPivotXValue = pivotXValue;
        mPivotYType = pivotYType;
        mPivotYValue = pivotYValue;
    }

    @Override
    protected void prepare() {
        mPivotX = resolveSize(mPivotXType, mPivotXValue, mChildWidth, mParentWidth);
        mPivotY = resolveSize(mPivotYType, mPivotYValue, mChildHeight, mParentHeight);

        if (mFillBefore) {
            if (mFromDegrees != 0f) {
                rotate(0f);
            }
        }
    }

    @Override
    protected void update(float progress) {
        resetIdentity();
        rotate(progress);
    }

    private void rotate(float progress) {
        float a = mFromDegrees;
        if (mFromDegrees != mToDegrees) {
            a = mFromDegrees + ((mToDegrees - mFromDegrees) * progress);
        }
//        Matrix.rotateM(mMatrix, 0, a, 0f, 0f, 1f);
        Matrix.setRotateM(mMatrix, 0, a, 0f, 0f, 1f);
    }
}
