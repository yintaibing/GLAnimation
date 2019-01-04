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
    private GLAnimation mGLAnimation;
    private GLTexture mGLTexture;

    public GLView(String mName) {
        this.mName = mName;
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

        int[] margin = getMargin();
        mMeasuredWidth = measureSize(parentWidth, mLayoutParams.width, mLayoutParams.widthRatio)
                - margin[0] - margin[2];
        mMeasuredHeight = measureSize(parentHeight, mLayoutParams.height, mLayoutParams.heightRatio)
                - margin[1] - margin[3];

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
        int[] margin = getMargin();
        switch (mLayoutParams.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.START:
            case Gravity.LEFT:
            default:
                mX = margin[0];
                break;

            case Gravity.END:
            case Gravity.RIGHT:
                mX = parentWidth - mMeasuredWidth - margin[2];
                break;

            case Gravity.CENTER_HORIZONTAL:
                mX = (int) ((parentWidth - mMeasuredWidth) * 0.5f);
                break;
        }
        switch (mLayoutParams.gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
            default:
                mY = parentHeight - mMeasuredHeight - margin[1];
                break;

            case Gravity.BOTTOM:
                mY = margin[3];
                break;

            case Gravity.CENTER_VERTICAL:
                mY = (int) ((parentHeight - mMeasuredHeight) * 0.5f);
                break;
        }
        mZ = mLayoutParams.z;
        mArrayVertexCoord = getVertexCoordArray(parentWidth, parentHeight,
                mX, mY, mZ, mMeasuredWidth, mMeasuredHeight);

        Log.e(TAG, "name=" + mName
                + " onLayout, "
                +" mX=" + mX + " mY=" + mY + " mZ=" + mZ
                + " vertexCoord=" + vertexCoordStr());
    }

    private String vertexCoordStr() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < mArrayVertexCoord.length; i++) {
            sb.append(mArrayVertexCoord[i]);
            sb.append(",");
            if (i > 0 && (i + 1) % 3 == 0) {
                sb.append("  ");
            }
        }
        sb.append("}");
        return sb.toString();
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

    private int[] getMargin() {
        int[] margin = new int[4];
        if (mLayoutParams != null && mLayoutParams.margin != null) {
            if (mLayoutParams.margin.length > 0) {
                margin[0] = mLayoutParams.margin[0];
            }
            if (mLayoutParams.margin.length > 1) {
                margin[1] = mLayoutParams.margin[1];
            }
            if (mLayoutParams.margin.length > 2) {
                margin[2] = mLayoutParams.margin[2];
            }
            if (mLayoutParams.margin.length > 3) {
                margin[3] = mLayoutParams.margin[3];
            }
        }
        return margin;
    }

    private static int measureSize(int parentSize, int paramsExactSize, float paramsSizeRatio) {
        return paramsExactSize > 0 ? paramsExactSize
                : paramsSizeRatio > 0 ? (int) (parentSize * paramsSizeRatio)
                : 0;
    }

    public static float[] getVertexCoordArray(int parentWidth, int parentHeight,
                                              int x, int y, float z, int width, int height) {
        float[] vertices = {
                x + width,  y + height, z,// 右上
                x + width,  y,          z,// 右下
                x,          y,          z,// 左下
                x,          y + height, z // 左上
        };
        /*
         * 上面是绝对坐标，参考系原点在屏幕左下角。OpenGL的标准化设备坐标（NDC），原点在屏幕中央。
         * 需要先平移-0.5*parentSize，然后除以0.5*parentSize，将坐标范围转到[-1,1]范围内。
         */
        float offsetX = parentWidth * 0.5f;
        float offsetY = parentHeight * 0.5f;
        for (int i = 0; i < vertices.length; i++) {
            switch (i % 3) {
                case 0:
                    // x
                    vertices[i] = (vertices[i] - offsetX) / offsetX;
                    break;

                case 1:
                    // y
                    vertices[i] = (vertices[i] - offsetY) / offsetY;
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
        public int[] margin;
        public float z;
    }
}
