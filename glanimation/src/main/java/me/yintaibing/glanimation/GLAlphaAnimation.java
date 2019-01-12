package me.yintaibing.glanimation;

public class GLAlphaAnimation extends GLColorFilterAnimation {
    private float mFromAlpha;
    private float mToAlpha;

    public GLAlphaAnimation(float fromAlpha, float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
    }

    @Override
    protected void update(float progress) {
        float alpha = mFromAlpha;
        if (mFromAlpha != mToAlpha) {
            alpha = mFromAlpha + ((mToAlpha - mFromAlpha) * progress);
        }
        mColorFilter[3] = alpha;
    }
}
