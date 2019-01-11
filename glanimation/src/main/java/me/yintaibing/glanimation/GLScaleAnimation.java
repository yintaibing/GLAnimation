package me.yintaibing.glanimation;

import android.opengl.Matrix;
import android.util.Log;

public class GLScaleAnimation extends GLAnimation {
    private static String TAG = "GLScaleAnimation";

    private float mFromX;
    private float mToX;
    private float mFromY;
    private float mToY;
    private int mPivotXType;
    private float mPivotXValue;
    private int mPivotYType;
    private float mPivotYValue;

    private float[] mPivot = new float[3];

    public GLScaleAnimation(float fromX, float toX, float fromY, float toY,
                            int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        mFromX = fromX;
        mToX = toX;
        mFromY = fromY;
        mToY = toY;
        mPivotXType = pivotXType;
        mPivotXValue = pivotXValue;
        mPivotYType = pivotYType;
        mPivotYValue = pivotYValue;
    }

    @Override
    public void prepare(int parentWidth, int parentHeight, GLView view) {
        super.prepare(parentWidth, parentHeight, view);
        mPivot[0] = Utils.toWorldCoord(resolveSize(mPivotXType, mPivotXValue,
                view.getMeasuredWidth(), parentWidth, view.getX()), parentWidth, false);
        mPivot[1] = Utils.toWorldCoord(resolveSize(mPivotYType, mPivotYValue,
                view.getMeasuredHeight(), parentHeight, view.getY()), parentHeight, false);
        mPivot[2] = 0f;

        Log.e(TAG, "mPivot=" + Utils.arrayToString(mPivot, 3));

        if (mFillBefore) {
            if (mFromX != 1f || mFromY != 1f) {
                scale(0f);
            }
        }
    }

    @Override
    protected void update(float progress) {
        resetIdentity();
        scale(progress);
    }

    private float resolveSize(int type, float value, int size, int parentSize, int base) {
        float coord = resolveSize(type, value, size, parentSize);
        if (!isRelativeToParent(type)) {
            coord += base;
        }
        return coord;
    }

    private void scale(float progress) {
        float sx = mFromX;
        float sy = mFromY;
        if (mFromX != mToX) {
            sx = mFromX + ((mToX - mFromX) * progress);
        }
        if (mFromY != mToY) {
            sy = mFromY + ((mToY - mFromY) * progress);
        }
        float tx = mPivot[0] * (1f / sx - 1f);
        float ty = mPivot[1] * (1f / sy - 1f);
        Matrix.scaleM(mMatrix, 0, sx, sy, 1f);
        Matrix.translateM(mMatrix, 0, tx, ty, 0f);
    }
}
