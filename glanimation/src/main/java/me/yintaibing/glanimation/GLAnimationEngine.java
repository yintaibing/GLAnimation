package me.yintaibing.glanimation;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.Buffer;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLAnimationEngine implements GLSurfaceView.Renderer {
    private List<GLView> mViews;

    private int[] mBuffers;
    private int mNextFreeBuffer;

    public int pushBufferData(Buffer data, int size) {
        int buffer = mBuffers[mNextFreeBuffer];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, size, data, GLES20.GL_STATIC_DRAW);
        return buffer;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
