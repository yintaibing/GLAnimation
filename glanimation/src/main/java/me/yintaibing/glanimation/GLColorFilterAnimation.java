package me.yintaibing.glanimation;

public abstract class GLColorFilterAnimation extends GLAnimation {
    protected float[] mColorFilter = Utils.getDefaultColorFilter();

    public GLColorFilterAnimation() {
        super();
    }

    public float[] getColorFilter() {
        return mColorFilter;
    }
}
