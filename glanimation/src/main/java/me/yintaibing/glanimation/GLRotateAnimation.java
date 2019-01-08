package me.yintaibing.glanimation;

public class GLRotateAnimation extends GLAnimation {
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
    protected void prepare() {
        mPivotStart[0] = resolveSize(mPivotXType, mPivotXValue, mChildWidth, mParentWidth);
        mPivotStart[1] = resolveSize(mPivotYType, mPivotYValue, mChildHeight, mParentHeight);
        mPivotStart[2] = 0f;
        mPivotNormalizedVector[0] = 0f;
        mPivotNormalizedVector[1] = 0f;
        mPivotNormalizedVector[2] = 1f;

        if (mFillBefore) {
            if (mFromDegrees != 0f) {
                rotate(0f);
            }
        }
    }

    @Override
    protected void update(float progress) {
        resetIdentity();
        rotate(progress);
    }

    private void rotate(float progress) {
        resetIdentity();
        float degrees = mFromDegrees;
        if (mFromDegrees != mToDegrees) {
            degrees = mFromDegrees + ((mToDegrees - mFromDegrees) * progress);
        }
//        Matrix.rotateM(mMatrix, 0, a, 0f, 0f, 1f);
//        Matrix.setRotateM(mMatrix, 0, a, 0f, 0f, 1f);
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

        float cos = (float) Math.cos(degrees);
        float sin = (float) Math.sin(degrees);

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
