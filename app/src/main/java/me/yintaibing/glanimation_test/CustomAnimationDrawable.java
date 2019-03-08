package me.yintaibing.glanimation_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

/**
 * 自定义AnimationDrawable
 *
 * @author yintaibing
 * @date 2017/11/30
 */

public class CustomAnimationDrawable extends AnimationDrawable {

    private boolean mMirror;
    private int mColorFilter;
    private Matrix mMatrix;
    private float mScaleX, mScaleY;

    public CustomAnimationDrawable() {
        this(1f, 1f, 0, false);
    }

    public CustomAnimationDrawable(float scaleX, float scaleY, int colorFilter, boolean mirror) {
        super();

        init(scaleX, scaleY, colorFilter, mirror);
    }

    public CustomAnimationDrawable(FrameParams params) {
        super();

        setFrameParams(params);
    }

    public void setFrameParams(FrameParams params) {
        init(params.scaleX, params.scaleY, params.colorFilter, params.mirror);
    }

    public void setScale(float scaleX, float scaleY) {
        mScaleX = scaleX;
        mScaleY = scaleY;
        if (mMatrix == null) {
            mMatrix = createMatrix(scaleX, scaleY);
        } else {
            mMatrix.setScale(scaleX, scaleY);
        }
    }

    private void init(float scaleX, float scaleY, int colorFilter, boolean mirror) {
        mMirror = mirror;
        mColorFilter = colorFilter;
//        mMatrix = createMatrix(mScaleX = scaleX, mScaleY = scaleY);
        setScale(scaleX, scaleY);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mMirror) {
            // 只处理一次
            mMirror = false;
            applyMirror(mMatrix);
        }
        BitmapDrawable current = (BitmapDrawable) getCurrent();
        applyColorFilter(current.getPaint(), mColorFilter);
        Bitmap b = current.getBitmap();
        if(b != null && !b.isRecycled()) {
            canvas.drawBitmap(b, mMatrix, current.getPaint());
        }

        int frameCount = getNumberOfFrames();
        if (mOnCompleteListener != null && current == getFrame(frameCount - 1)) {
            // 这里还没等最后一帧画完就调用了，所以回调时机其实是不完全准确的。
            mOnCompleteListener.onComplete();
            if (mOnCompleteListener.isOneShot()) {
                mOnCompleteListener = null;
            }
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mScaleX != 1f ? (int) (mScaleX * super.getIntrinsicWidth()) :
                super.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mScaleY != 1f ? (int) (mScaleY * super.getIntrinsicHeight()) :
                super.getIntrinsicHeight();
    }

    private Matrix createMatrix(float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        if (scaleX != 1f || scaleY != 1f) {
            matrix.postScale(scaleX, scaleY);
        }
        return matrix;
    }

    private void applyMirror(Matrix matrix) {
        matrix.postScale(-1f, 1f);
        matrix.postTranslate(getIntrinsicWidth(), 0f);
    }

    private void applyColorFilter(Paint paint, int colorFilter) {
        if (paint == null || colorFilter == 0) {
            return;
        }
        if (paint.getColorFilter() == null) {
            paint.setColorFilter(new ColorMatrixColorFilter(parseColorMatrix(colorFilter)));
        }
    }

    private ColorMatrix parseColorMatrix(int color) {
        float r = Color.red(color) / 255f;
        float g = Color.green(color) / 255f;
        float b = Color.blue(color) / 255f;
        float a = Color.alpha(color) / 255f;
        return new ColorMatrix(new float[]{
                r, 0, 0, 0, 0,
                0, g, 0, 0, 0,
                0, 0, b, 0, 0,
                0, 0, 0, a, 0
        });
    }

    private OnCompleteListener mOnCompleteListener;
    public void setOnCompleteListener(OnCompleteListener l) {
        mOnCompleteListener = l;
    }
    public static abstract class OnCompleteListener {
        abstract void onComplete();

        boolean isOneShot() {
            return true;
        }
    }
}
