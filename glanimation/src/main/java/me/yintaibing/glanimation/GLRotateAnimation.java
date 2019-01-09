package me.yintaibing.glanimation;

import android.util.Log;

public class GLRotateAnimation extends GLAnimation {
    private static final String TAG = "GLRotateAnimation";

    private float mFromDegrees;
    private float mToDegrees;
    private int mPivotXType;
    private float mPivotXValue;
    private int mPivotYType;
    private float mPivotYValue;

    private float[] mPivotStart = new float[3];
    private float[] mPivotNormalizedVector = new float[3];

    public GLRotateAnimation(float fromDegrees, float toDegrees,
                             int pivotXType, float pivotXValue,
                             int pivotYType, float pivotYValue) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mPivotXType = pivotXType;
        mPivotXValue = pivotXValue;
        mPivotYType = pivotYType;
        mPivotYValue = pivotYValue;
    }

    @Override
    public void prepare(int parentWidth, int parentHeight, GLView view) {
        super.prepare(parentWidth, parentHeight, view);
        mPivotStart[0] = Utils.toWorldCoord(resolveSize(mPivotXType, mPivotXValue,
                view.getMeasuredWidth(), parentWidth, view.getX()), parentWidth, false);
        mPivotStart[1] = Utils.toWorldCoord(resolveSize(mPivotYType, mPivotYValue,
                view.getMeasuredHeight(), parentHeight, view.getY()), parentHeight, false);
        mPivotStart[2] = 0f;

        mPivotNormalizedVector[0] = 0f;
        mPivotNormalizedVector[1] = 0f;
        mPivotNormalizedVector[2] = 1f;

        Log.e(TAG, "mPivotStart=" + Utils.arrayToString(mPivotStart, 3));

        if (mFillBefore) {
            if (mFromDegrees != 0f) {
                rotate(0f);
            }
        }
    }

    @Override
    protected void update(float progress) {
        rotate(progress);
    }

    private float resolveSize(int type, float value, int size, int parentSize, int base) {
        float coord = resolveSize(type, value, size, parentSize);
        if (!isRelativeToParent(type)) {
            coord += base;
        }
        return coord;
    }

    private void rotate(float progress) {
        float degrees = mFromDegrees;
        if (mFromDegrees != mToDegrees) {
            degrees = mFromDegrees + ((mToDegrees - mFromDegrees) * progress);
        }
        setRotateM(mMatrix, mPivotStart, mPivotNormalizedVector, degrees);
    }

    /**
     * 三维绕任意轴旋转的矩阵运算。传入的旋转轴向量需要是单位向量。
     *
     * @param rm                    存储结果的矩阵
     * @param pivotStart            旋转轴向量的起点
     * @param pivotNormalizedVector 旋转轴单位向量
     * @param degrees               旋转角度
     */
    private void setRotateM(float[] rm, float[] pivotStart, float[] pivotNormalizedVector, float degrees) {
        float a = pivotStart[0];// start.x
        float b = pivotStart[1];// start.y
        float c = pivotStart[2];// start.z

        float u = pivotNormalizedVector[0];// axis.x
        float v = pivotNormalizedVector[1];// axis.y
        float w = pivotNormalizedVector[2];// axis.z

        float uu = u * u;
        float uv = u * v;
        float uw = u * w;
        float vv = v * v;
        float vw = v * w;
        float ww = w * w;
        float au = a * u;
        float av = a * v;
        float aw = a * w;
        float bu = b * u;
        float bv = b * v;
        float bw = b * w;
        float cu = c * u;
        float cv = c * v;
        float cw = c * w;

        double radians = (degrees * Math.PI) / 180d;// 角度转弧度
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        rm[0] = uu + (vv + ww) * cos;
        rm[1] = uv * (1f - cos) + w * sin;
        rm[2] = uw * (1f - cos) - v * sin;
        rm[3] = 0f;

        rm[4] = uv * (1f - cos) - w * sin;
        rm[5] = vv + (uu + ww) * cos;
        rm[6] = vw * (1f - cos) + u * sin;
        rm[7] = 0f;

        rm[8] = uw * (1f - cos) + v * sin;
        rm[9] = vw * (1f - cos) - u * sin;
        rm[10] = ww + (uu + vv) * cos;
        rm[11] = 0f;

        rm[12] = (a * (vv + ww) - u * (bv + cw)) * (1 - cos) + (bw - cv) * sin;
        rm[13] = (b * (uu + ww) - v * (au + cw)) * (1 - cos) + (cu - aw) * sin;
        rm[14] = (c * (uu + vv) - w * (au + bv)) * (1 - cos) + (av - bu) * sin;
        rm[15] = 1f;
    }
}
