package me.yintaibing.glanimation_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import me.yintaibing.glanimation_test.R;

public class TestGLPainter {
    private static final int BYTES_OF_SHORT = 2;
    private static final int BYTES_OF_FLOAT = 4;

    public float width, height;

    private float[] mVertexPositionArray = new float[]{
            0.5f, 0.5f, 0f,
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f
    };

    private float[] mVertexPositionArray2 = new float[] {
            1f, 0f, 0f,
            0f, 0f, 0f,
            0f, -1.5f, 0f,
            1f, -1.5f, 0f
    };

    private short[] mVertexIndexArray = new short[] {
            0, 1, 2,
            2, 3, 0
    };

    private float[] mVertexColorArray = new float[] {
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 1f, 1f, 1f,
            1f, 1f, 0f, 1f
    };

    private float[] mTexturePositionArray = new float[] {
//            1f, 1f, 0f, 1f, 0f, 0f, 1f, 0f
            // y取反
            1f, 0f, 0f, 0f, 0f, 1f, 1f, 1f
    };

    private float[] mMVPMatrix = new float[16];
    private float[] mMVPMatrix2 = new float[16];
    private float[] mViewMatrix2 = new float[16];
    private float[] mProjMatrix2 = new float[16];
    private float[] mModelMatrix2 = new float[16];
    private float[] mScaleMatrix = new float[16];
    private float[] mTranslateMatrix = new float[16];
    private float[] mTempMatrix = new float[16];

    private int mVertexShader;
    private int mFragmentShader;

    private int[] mGLBuffers = new int[5];

    private Buffer mVertexPositionBuffer;
    private Buffer mVertexPositionBuffer2;
    private Buffer mVertexColorBuffer;
    private Buffer mTexturePositionBuffer;
    private Buffer mVertexIndexBuffer;

    private int mGLProgram;
    private int maPositionLocation;
    private int maColorLocation;
    private int muMVPLocation;
    private int maCoordLocation;
    private int muSampler2DLocation;
    private int muAlphaLocation;
    private float[] alpha = {1f};

    private int mTextureID;

    public GLSurfaceView mGLSurfaceView;

    public void prepare(Context context) {
        // vertex buffer
        mVertexPositionBuffer = toFloatBuffer(mVertexPositionArray);
        mVertexPositionBuffer2 = toFloatBuffer(mVertexPositionArray2);
        mVertexIndexBuffer = toShortBuffer(mVertexIndexArray);
        mVertexColorBuffer = toFloatBuffer(mVertexColorArray);
        mTexturePositionBuffer = toFloatBuffer(mTexturePositionArray);

        GLES20.glGenBuffers(mGLBuffers.length, mGLBuffers, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mGLBuffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                mVertexPositionArray.length * BYTES_OF_FLOAT,
                mVertexPositionBuffer,
                GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mGLBuffers[1]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                mVertexPositionArray2.length * BYTES_OF_FLOAT,
                mVertexPositionBuffer2,
                GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mGLBuffers[2]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                mVertexColorArray.length * BYTES_OF_FLOAT,
                mVertexColorBuffer,
                GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mGLBuffers[3]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                mTexturePositionArray.length * BYTES_OF_FLOAT,
                mTexturePositionBuffer,
                GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mGLBuffers[4]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                mVertexIndexArray.length * BYTES_OF_SHORT,
                mVertexIndexBuffer,
                GLES20.GL_STATIC_DRAW);

        // shader
        mVertexShader = loadShaderFromRaw(GLES20.GL_VERTEX_SHADER, context, R.raw.vertex_shader);
        mFragmentShader = loadShaderFromRaw(GLES20.GL_FRAGMENT_SHADER, context, R.raw.fragment_shader);

        // program
        mGLProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mGLProgram, mVertexShader);
        GLES20.glAttachShader(mGLProgram, mFragmentShader);
        GLES20.glLinkProgram(mGLProgram);

        // aPosition handle
        maPositionLocation = GLES20.glGetAttribLocation(mGLProgram, "aPosition");
        // aColor handle
        maColorLocation = GLES20.glGetAttribLocation(mGLProgram, "aColor");
        // uMVPLocation
        muMVPLocation = GLES20.glGetUniformLocation(mGLProgram, "uMVP");
        // maCoordLocation
        maCoordLocation = GLES20.glGetAttribLocation(mGLProgram, "aCoord");
        // uSamplerLocation
        muSampler2DLocation = GLES20.glGetUniformLocation(mGLProgram, "uSampler2D");
        // muAlphaLocation
        muAlphaLocation = GLES20.glGetUniformLocation(mGLProgram, "uAlpha");

        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.setIdentityM(mScaleMatrix, 0);
        Matrix.setIdentityM(mMVPMatrix2, 0);
        Matrix.setIdentityM(mTranslateMatrix, 0);

        mTextureID = loadTextureFromDrawable(context, R.mipmap.test);

        // enable cull face
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CCW);
        GLES20.glUseProgram(mGLProgram);
    }

    public void scale(float scale) {
        Matrix.setIdentityM(mMVPMatrix, 0);
//        Matrix.setLookAtM(mMVPMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f);

//        Matrix.setIdentityM(mScaleMatrix, 0);
////        Matrix.scaleM(mScaleMatrix, 0, scale, scale, 1f);
//        Matrix.multiplyMM(mMVPMatrix, 0, mScaleMatrix, 0, mMVPMatrix, 0);
        this.alpha[0] = scale;
    }

    public void translate(float translate) {
//        float real = 1f - translate;
        Matrix.setIdentityM(mMVPMatrix2, 0);
//        Matrix.setIdentityM(mProjMatrix2, 0);
//        Matrix.setIdentityM(mViewMatrix2, 0);
//        Matrix.setIdentityM(mModelMatrix2, 0);
//        Matrix.setIdentityM(mTranslateMatrix, 0);
//
        float ratio= width > height ? (float)width / height : (float)height / width;
        if (width > height) {
            // 横屏
            Matrix.orthoM(mProjMatrix2, 0, -ratio, ratio, -1, 1, 0, 1);
        } else {
            Matrix.orthoM(mProjMatrix2, 0, -1, 1, -ratio, ratio, 0, 1);
        }
//        Matrix.orthoM(mProjMatrix2, 0, -width*0.5f, width*0.5f, -height*0.5f, height*0.5f, 1,0);
        Matrix.rotateM(mTranslateMatrix, 0, 360 * (1f- translate), 0f, 0f, 1f);

        Matrix.multiplyMM(mMVPMatrix2, 0, mTranslateMatrix, 0, mMVPMatrix2, 0);
//        mTempMatrix = mModelMatrix2.clone();
        Matrix.multiplyMM(mMVPMatrix2, 0, mViewMatrix2, 0, mMVPMatrix2, 0);
//        mTempMatrix = mMVPMatrix2.clone();
        Matrix.multiplyMM(mMVPMatrix2, 0, mProjMatrix2, 0, mMVPMatrix2, 0);

//        alpha = translate;
    }

    public void draw() {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
        GLES20.glUniform1i(muSampler2DLocation, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mGLBuffers[0]);
        GLES20.glVertexAttribPointer(maPositionLocation, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(maPositionLocation);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mGLBuffers[2]);
        GLES20.glVertexAttribPointer(maColorLocation, 4, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(maColorLocation);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mGLBuffers[3]);
        GLES20.glVertexAttribPointer(maCoordLocation, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(maCoordLocation);

        GLES20.glUniformMatrix4fv(muMVPLocation, 1, false, mMVPMatrix, 0);
//        GLES20.glUniform1fv(muAlphaLocation, 1, this.alpha, 0);
        GLES20.glUniform4f(muAlphaLocation, 1f, 1f, 1f, alpha[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mGLBuffers[4]);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, 0);


        // another draw
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mGLBuffers[1]);
//        GLES20.glVertexAttribPointer(maPositionLocation, 3, GLES20.GL_FLOAT, false, 0, 0);
//        GLES20.glEnableVertexAttribArray(maPositionLocation);
//
//        GLES20.glUniformMatrix4fv(muMVPLocation, 1, false, mMVPMatrix2, 0);
//
////        GLES20.glEnable(GLES20.GL_BLEND);
////        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
////        GLES20.glBlendColor(1f, 1f, 1f, 0.5f);
//
////        GLES20.glUniform1f(muAlphaLocation, 1f);
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mGLBuffers[4]);
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, 0);
    }

    private static int loadShader(int shaderType, String sourceCode) {
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, sourceCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    private static int loadShaderFromRaw(int shaderType, Context context, int resId) {
        InputStream in = context.getResources().openRawResource(resId);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            in.close();
            String sourceCode = baos.toString();
            baos.close();
            return loadShader(shaderType, sourceCode);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int loadTextureFromDrawable(Context context, int resId) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);

            int[] textures = new int[1];
            GLES20.glGenTextures(textures.length, textures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
            GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_REPEAT);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            bitmap.recycle();

            return textures[0];
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static Buffer toFloatBuffer(float[] vertex) {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertex.length * BYTES_OF_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(vertex).position(0);
        return fb;
    }

    private static Buffer toShortBuffer(short[] vertex) {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertex.length * BYTES_OF_SHORT);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer sb = bb.asShortBuffer();
        sb.put(vertex).position(0);
        return sb;
    }
}