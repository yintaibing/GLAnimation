package me.yintaibing.glanimation;

import android.opengl.Matrix;

public class GLTranslateAnimation extends GLAnimation {
    // 暂时都是RELATIVE_TO_SELF
    private float mFromXRatio;
    private float mFromYRatio;
    private float mToXRatio;
    private float mToYRatio;

    public GLTranslateAnimation(float mFromXRatio, float mFromYRatio, float mToXRatio, float mToYRatio) {
        this.mFromXRatio = mFromXRatio;
        this.mFromYRatio = mFromYRatio;
        this.mToXRatio = mToXRatio;
        this.mToYRatio = mToYRatio;
    }

    @Override
    protected void prepare() {
        Matrix.setIdentityM(mMatrix, 0);
//        if (mFillBefore) {
//            float dx = mChildWidth * mFromXRatio;
//            float dy = mChildHeight * mFromYRatio;
//            if (dx != 0 || dy != dy) {
//                Matrix.translateM(mMatrix, 0, dx / mChildWidth, dy / mChildHeight, 0);
//            }
//        }
    }

    @Override
    protected void update(float progress) {
        Matrix.setIdentityM(mMatrix, 0);
//        float totalDx = mChildWidth * (mToXRatio - mFromXRatio);
//        float currentDx = totalDx * progress;
//        float totalDy = mChildHeight * (mFromYRatio - mToYRatio);// Android Y轴与OpenGL方向相反
//        float currentDy = totalDy * progress;
//        Matrix.translateM(mMatrix, 0, currentDx / mChildWidth, currentDy / mChildHeight, 0);
        Matrix.scaleM(mMatrix, 0, progress, progress, 1);
    }
}
