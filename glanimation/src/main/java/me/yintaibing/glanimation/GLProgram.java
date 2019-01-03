package me.yintaibing.glanimation;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class GLProgram {
    private static final String TAG = "GLProgram";

    private int mProgramHandle;

    private int attribute_vertex_coord;
    private int attribute_texture_coord;
    private int uniform_mvp_matrix;
    private int uniform_texture;
    private int uniform_multiply_color;

    public GLProgram(Context context) {
        int vertexShader = compileShader(GLES20.GL_VERTEX_SHADER,
                Utils.readRaw(context, R.raw.glanimation_vertex_shader));
        int fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER,
                Utils.readRaw(context, R.raw.glanimation_fragment_shader));
        mProgramHandle = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgramHandle, vertexShader);
        GLES20.glAttachShader(mProgramHandle, fragmentShader);
        GLES20.glLinkProgram(mProgramHandle);

        attribute_vertex_coord = GLES20.glGetAttribLocation(mProgramHandle,
                "attribute_vertex_coord");
        attribute_texture_coord = GLES20.glGetAttribLocation(mProgramHandle,
                "attribute_texture_coord");
        uniform_mvp_matrix = GLES20.glGetUniformLocation(mProgramHandle,
                "uniform_mvp_matrix");
        uniform_texture  = GLES20.glGetUniformLocation(mProgramHandle,
                "uniform_texture");
        uniform_multiply_color = GLES20.glGetUniformLocation(mProgramHandle,
                "uniform_multiply_color");

        Log.e(TAG, "program linked, mProgramHandle=" + mProgramHandle
                + " avc=" + attribute_vertex_coord
                + " atc=" + attribute_texture_coord
                + " umm=" + uniform_mvp_matrix
                + " ut=" + uniform_texture
                + " umc=" + uniform_multiply_color);
    }

    public void use() {
        GLES20.glUseProgram(mProgramHandle);
        Log.e(TAG, "glUseProgram");
    }

    public int getAttribute_vertex_coord() {
        return attribute_vertex_coord;
    }

    public int getAttribute_texture_coord() {
        return attribute_texture_coord;
    }

    public int getUniform_mvp_matrix() {
        return uniform_mvp_matrix;
    }

    public int getUniform_texture() {
        return uniform_texture;
    }

    public int getUniform_multiply_color() {
        return uniform_multiply_color;
    }

    private static int compileShader(int shaderType, String srcCode) {
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, srcCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
