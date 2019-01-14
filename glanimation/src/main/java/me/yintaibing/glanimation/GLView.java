package me.yintaibing.glanimation;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Gravity;

import java.nio.FloatBuffer;

public class GLView {
    private static final String TAG = "GLView";

    public String mName;
    // 这个View的左下角
    private GLLayoutParams mLayoutParams;
    private int mX;
    private int mY;
    private float mZ;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private boolean mNeedRelayout = true;
    private boolean mVisible;

    private float[] mArrayVertexCoord;// 顶点坐标数组，length = 4 vertices * 3 dimension
//    private Buffer mBufferVertexCoord;// 顶点坐标缓冲
    private int mBufferVertexCoord;
    private float[] mArrayVertexColor;// 顶点颜色数组，length = 4 vertices * 4 dimension
//    private Buffer mBufferVertexColor;// 顶点颜色缓冲

    private float[] mModelMatrix = new float[16];
//    private float[] mColorFilter = Utils.getDefaultColorFilter();
    private GLAnimation mGLAnimation;
    private GLTexture mGLTexture;

    public GLView(String mName) {
        this.mName = mName;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    public int getMeasuredHeight() {
        return mMeasuredHeight;
    }

    public GLLayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    public void setLayoutParams(GLLayoutParams mLayoutParams) {
        this.mLayoutParams = mLayoutParams;
    }

    public int getNeedBufferCount() {
        int count = 0;
        if (mArrayVertexCoord != null) {
            count++;
        }
        if (mArrayVertexColor != null) {
            count++;
        }
        return count;
    }

    public float[] getArrayVertexCoord() {
        return mArrayVertexCoord;
    }

    public int getBufferVertexCoord() {
        return mBufferVertexCoord;
    }

    public float[] getModelMatrix() {
        Matrix.setIdentityM(mModelMatrix, 0);
        if (mGLAnimation != null) {
            Matrix.multiplyMM(mModelMatrix, 0, mGLAnimation.getMatrix(), 0, mModelMatrix, 0);
        }
        return mModelMatrix;
    }

    public float[] getColorFilter() {
        if (mGLAnimation != null && mGLAnimation instanceof GLColorFilterAnimation) {
            return ((GLColorFilterAnimation) mGLAnimation).getColorFilter();
        }
        return Utils.getDefaultColorFilter();
    }

    public GLAnimation getAnimation() {
        return mGLAnimation;
    }

    public void setAnimation(GLAnimation mGLAnimation) {
        this.mGLAnimation = mGLAnimation;
    }

    public GLTexture getTexture() {
        return mGLTexture;
    }

    public void setTexture(GLTexture mGLTexture) {
        this.mGLTexture = mGLTexture;
    }

    public void onMeasure(int parentWidth, int parentHeight) {
        if (!mNeedRelayout || mLayoutParams == null) {
            return;
        }

        mMeasuredWidth = measureSize(mLayoutParams.width, mLayoutParams.widthRatio, parentWidth)
                - mLayoutParams.leftMargin - mLayoutParams.rightMargin;
        mMeasuredHeight = measureSize(mLayoutParams.height, mLayoutParams.heightRatio, parentHeight)
                - mLayoutParams.topMargin - mLayoutParams.bottomMargin;

        Log.e(TAG, "name=" + mName
                + " onMeasure, mMeasuredWidth=" + mMeasuredWidth
                + " mMeasuredHeight=" + mMeasuredHeight);
    }

    public void onLayout(int left, int top, int right, int bottom) {
        if (!mNeedRelayout || mLayoutParams == null) {
            return;
        }

        int parentWidth = Math.abs(right - left);
        int parentHeight = Math.abs(bottom - top);
        switch (mLayoutParams.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.START:
            case Gravity.LEFT:
            default:
                mX = mLayoutParams.leftMargin;
                break;

            case Gravity.END:
            case Gravity.RIGHT:
                mX = parentWidth - mMeasuredWidth - mLayoutParams.rightMargin;
                break;

            case Gravity.CENTER_HORIZONTAL:
                mX = (int) ((parentWidth - mMeasuredWidth) * 0.5f);
                break;
        }
        switch (mLayoutParams.gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
            default:
                mY = parentHeight - mMeasuredHeight - mLayoutParams.topMargin;
                break;

            case Gravity.BOTTOM:
                mY = mLayoutParams.bottomMargin;
                break;

            case Gravity.CENTER_VERTICAL:
                mY = (int) ((parentHeight - mMeasuredHeight) * 0.5f);
                break;
        }
        mZ = mLayoutParams.z;
        mArrayVertexCoord = createVertexCoordArray(parentWidth, parentHeight);

        Log.e(TAG, "name=" + mName
                + " onLayout, "
                +" mX=" + mX + " mY=" + mY + " mZ=" + mZ
                + " vertexCoord=" + Utils.arrayToString(mArrayVertexCoord, 3));
    }

    public int saveBuffers(int[] buffers, int bufferIndex) {
        mBufferVertexCoord = buffers[bufferIndex];
        FloatBuffer vertexCoordBufferData = Utils.toFloatBuffer(mArrayVertexCoord);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBufferVertexCoord);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mArrayVertexCoord.length * Utils.BYTES_OF_FLOAT,
                vertexCoordBufferData, GLES20.GL_STATIC_DRAW);
        return 1;
    }

    public void onPreDraw(GLAnimationEngine engine) {
//        mBufferVertexCoord = engine.pollBuffer();
//        FloatBuffer vertexCoordBufferData = Utils.toFloatBuffer(mArrayVertexCoord);
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBufferVertexCoord);
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mArrayVertexCoord.length * Utils.BYTES_OF_FLOAT,
//                vertexCoordBufferData, GLES20.GL_STATIC_DRAW);
    }

    public void onDraw(GLAnimationEngine engine) {
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBufferVertexCoord);
//        GLES20.glVertexAttribPointer(engine.getAttribute_vertex_coord(), 3, GLES20.GL_FLOAT, false,
//                0, 0);
//        GLES20.glEnableVertexAttribArray(engine.getAttribute_vertex_coord());
    }

    private static int measureSize(int paramsExactSize, float paramsSizeRatio, int parentSize) {
        return paramsExactSize > 0 ? paramsExactSize
                : paramsSizeRatio > 0 ? (int) (parentSize * paramsSizeRatio)
                : 0;
    }

    public float[] createVertexCoordArray(int parentWidth, int parentHeight) {
        float[] vertices = {
                mX + mMeasuredWidth,  mY + mMeasuredHeight, mZ,// 右上
                mX + mMeasuredWidth,  mY,                   mZ,// 右下
                mX,                   mY,                   mZ,// 左下
                mX,                   mY + mMeasuredHeight, mZ // 左上
        };
        /*
         * 上面是绝对坐标，参考系原点在屏幕左下角。OpenGL的标准化设备坐标（NDC），原点在屏幕中央。
         * 需要先平移-0.5*parentSize，然后除以0.5*parentSize，将坐标范围转到[-1,1]范围内。
         */
        for (int i = 0; i < vertices.length; i++) {
            switch (i % 3) {
                case 0:
                    // x
                    vertices[i] = Utils.toWorldCoord(vertices[i], parentWidth, false);
                    break;

                case 1:
                    // y
                    vertices[i] = Utils.toWorldCoord(vertices[i], parentHeight, false);
                    break;
            }
        }
        return vertices;
    }

    public static class GLLayoutParams {
        public int gravity;
        public int width;
        public float widthRatio;
        public int height;
        public float heightRatio;
        public float z;
        public int leftMargin;
        public int topMargin;
        public int rightMargin;
        public int bottomMargin;
    }
}
