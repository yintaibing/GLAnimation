package me.yintaibing.glanimation;

import android.opengl.Matrix;
import android.util.Log;

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
    public void prepare(int parentWidth, int parentHeight, GLView view) {
        super.prepare(parentWidth, parentHeight, view);
        mFromXDelta = Utils.toWorldCoord(resolveSize(mFromXType, mFromXValue,
                view.getMeasuredWidth(), parentWidth, view.getX()), parentWidth, true);
        mFromYDelta = Utils.toWorldCoord(resolveSize(mFromYType, mFromYValue,
                view.getMeasuredHeight(), parentHeight, view.getY()), parentHeight, true);
        mToXDelta = Utils.toWorldCoord(resolveSize(mToXType, mToXValue,
                view.getMeasuredWidth(), parentWidth, view.getX()), parentWidth, true);
        mToYDelta = Utils.toWorldCoord(resolveSize(mToYType, mToYValue,
                view.getMeasuredHeight(), parentHeight, view.getY()), parentHeight, true);

        Log.e(TAG, "mFromXDelta=" + mFromXDelta
                + " mFromYDelta=" + mFromYDelta
                + " mToXDelta=" + mToXDelta
                + " mToYDelta=" + mToYDelta);

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

    private float resolveSize(int type, float value, int size, int parentSize, int base) {
        float coord = resolveSize(type, value, size, parentSize);
        if (isRelativeToParent(type)) {
            coord -= base;
        }
        return coord;
    }

    private void translate(float progress) {
        float tx = mFromXDelta;
        float ty = mFromYDelta;
        if (mFromXDelta != mToXDelta) {
            tx = mFromXDelta + ((mToXDelta - mFromXDelta) * progress);
        }
        if (mFromYDelta != mToYDelta) {
            ty = mFromYDelta + ((mToYDelta - mFromYDelta) * progress);
        }
        Matrix.translateM(mMatrix, 0, tx, ty, 0f);
    }
}
