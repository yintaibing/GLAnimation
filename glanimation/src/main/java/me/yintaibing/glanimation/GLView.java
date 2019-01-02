package me.yintaibing.glanimation;

import android.view.Gravity;
import android.view.ViewGroup;

import java.nio.Buffer;

public class GLView {
    // 顶点绘制顺序索引数组
    private static final short[] ARRAY_VERTEX_INDEX = {
            0, 1, 2,
            2, 3, 0
    };
    // 顶点绘制顺序索引缓冲
    private static final Buffer BUFFER_VERTEX_INDEX = Utils.toShortBuffer(ARRAY_VERTEX_INDEX);
    // 纹理顶点绘制顺序索引数组
    private static final short[] ARRAY_TEXTURE_VERTEX_INDEX = {
            // 1f, 1f, 0f, 1f, 0f, 0f, 1f, 0f
            // OpenGL坐标与纹理坐标Y轴相反，所以坐标y取反
            1, 0, 0, 0, 0, 1, 1, 1
    };
    // 纹理顶点绘制顺序索引缓冲
    private static final Buffer BUFFER_TEXTURE_VERTEX_INDEX = Utils.toShortBuffer(
            ARRAY_TEXTURE_VERTEX_INDEX);


    // 这个View的左下角
    private GLLayoutParams mLayoutParams;
    private int mX;
    private int mY;
    private int mZ;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private boolean mNeedRelayout = true;
    private boolean mVisible;

    private float[] mArrayVertexCoord;// 顶点坐标数组，length = 4 vertices * 3 dimension
    private Buffer mBufferVertexCoord;// 顶点坐标缓冲
    private float[] mArrayVertexColor;// 顶点颜色数组，length = 4 vertices * 4 dimension
    private Buffer mBufferVertexColor;// 顶点颜色缓冲

    public int getNeedBufferCount() {
        int count = 0;
        if (mBufferVertexCoord != null) {
            count++;
        }
        if (mBufferVertexColor != null) {
            count++;
        }
        return count;
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
    }

    public void onLayout(int left, int top, int right, int bottom) {
        if (!mNeedRelayout || mLayoutParams == null) {
            return;
        }

        int parentWidth = right - left;
        int parentHeight = bottom - top;
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
                mY = margin[1];
                break;

            case Gravity.BOTTOM:
                mY = parentHeight - mMeasuredHeight - margin[3];
                break;

            case Gravity.CENTER_VERTICAL:
                mY = (int) ((parentHeight - mMeasuredHeight) * 0.5f);
                break;
        }
        mZ = mLayoutParams.z;
        mArrayVertexCoord = getVertexCoordArray(parentWidth, parentHeight,
                mX, mY, mZ, mMeasuredWidth, mMeasuredHeight);
        mBufferVertexCoord = Utils.toFloatBuffer(mArrayVertexCoord);
    }

    public void onPreDraw(GLAnimationEngine engine) {

    }

    public void onDraw() {

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
                                              int x, int y, int z, int width, int height) {
//        PointF rightTop = new PointF(x + width, y + height);
//        PointF leftTop = new PointF(x, y + height);
//        PointF leftBottom = new PointF(x, y);
//        PointF rightBottom = new PointF(x + width, y);

        // OpenGL坐标系原点在(parentWidth/2, parentHeight/2)处，相当于坐标反向平移
        float offsetX = parentWidth * -0.5f;
        float offsetY = parentHeight * -0.5f;

        // 右上，左上，左下，右下
        return new float[]{
                x + width + offsetX, y + height + offsetY, z,
                x + offsetX, y + height + offsetY, z,
                x + offsetX, y + offsetY, z,
                x + width + offsetX, y + offsetY, z
        };
    }

    public static class GLLayoutParams {
        public int gravity;
        public int width;
        public float widthRatio;
        public int height;
        public float heightRatio;
        public int[] margin;
        public int z;
    }
}
