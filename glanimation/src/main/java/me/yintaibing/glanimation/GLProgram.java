package me.yintaibing.glanimation;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class GLProgram {
    private static final String TAG = "GLProgram";

    private int mProgramHandle;

    private int attribute_vertex_coord;
    private int attribute_texture_coord;
    private int attribute_texture_coord_alpha;
    private int uniform_mvp_matrix;
    private int uniform_texture;
    private int uniform_texture_alpha;
    private int uniform_color_filter;
    private int uniform_debug;

    private int uniform_debug_value;

    public GLProgram(Context context) {
        int vertexShader = Utils.compileShader(GLES20.GL_VERTEX_SHADER,
                Utils.readRaw(context, R.raw.glanimation_vertex_shader));
        int fragmentShader = Utils.compileShader(GLES20.GL_FRAGMENT_SHADER,
                Utils.readRaw(context, R.raw.glanimation_fragment_shader));
        mProgramHandle = Utils.linkProgram(vertexShader, fragmentShader);

        attribute_vertex_coord = GLES20.glGetAttribLocation(mProgramHandle,
                "attribute_vertex_coord");
        attribute_texture_coord = GLES20.glGetAttribLocation(mProgramHandle,
                "attribute_texture_coord");
        attribute_texture_coord_alpha = GLES20.glGetAttribLocation(mProgramHandle,
                "attribute_texture_coord_alpha");
        uniform_mvp_matrix = GLES20.glGetUniformLocation(mProgramHandle,
                "uniform_mvp_matrix");
        uniform_texture  = GLES20.glGetUniformLocation(mProgramHandle,
                "uniform_texture");
        uniform_texture_alpha = GLES20.glGetUniformLocation(mProgramHandle,
                "uniform_texture_alpha");
        uniform_color_filter = GLES20.glGetUniformLocation(mProgramHandle,
                "uniform_color_filter");
        uniform_debug = GLES20.glGetUniformLocation(mProgramHandle,
                "uniform_debug");

        Log.e(TAG, "program linked, mProgramHandle=" + mProgramHandle
                + " vertex_coord=" + attribute_vertex_coord
                + " texture_coord=" + attribute_texture_coord
                + " texture_coord_alpha=" + attribute_texture_coord_alpha
                + " mvp=" + uniform_mvp_matrix
                + " texture=" + uniform_texture
                + " texture_alpha" + uniform_texture_alpha
                + " color_filter=" + uniform_color_filter
                + " debug=" + uniform_debug);
    }

    public void setDebug(boolean debug) {
        uniform_debug_value = debug ? 1 : 0;
    }

    public void makeDebug() {
        GLES20.glUniform1i(uniform_debug, uniform_debug_value);
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

    public int getAttribute_texture_coord_alpha() {
        return attribute_texture_coord_alpha;
    }

    public int getUniform_mvp_matrix() {
        return uniform_mvp_matrix;
    }

    public int getUniform_texture() {
        return uniform_texture;
    }

    public int getUniform_texture_alpha() {
        return uniform_texture_alpha;
    }

    public int getUniform_color_filter() {
        return uniform_color_filter;
    }
}
