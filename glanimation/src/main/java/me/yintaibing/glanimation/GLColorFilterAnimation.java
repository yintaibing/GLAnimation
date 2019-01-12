package me.yintaibing.glanimation;

public abstract class GLColorFilterAnimation extends GLAnimation {
    protected float[] mColorFilter = Utils.DEFAULT_COLOR_FILTER;

    public GLColorFilterAnimation() {
        super();
    }

    public float[] getColorFilter() {
        return mColorFilter;
    }
}
