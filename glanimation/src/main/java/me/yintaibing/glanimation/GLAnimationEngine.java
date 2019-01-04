package me.yintaibing.glanimation;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLAnimationEngine implements GLSurfaceView.Renderer {
    private static final String TAG = "GLAnimationEngine";

    // 顶点绘制顺序索引数组
    private static final short[] ARRAY_VERTEX_INDEX = {
            0, 1, 2,
            2, 3, 0
    };
    // 顶点绘制顺序索引缓冲
    private static int BUFFER_VERTEX_INDEX;
    // 纹理顶点绘制顺序索引数组
    private static final float[] ARRAY_TEXTURE_VERTEX = {
            // 1f, 1f, 1f, 0f, 0f, 0f, 0f, 1f
            // OpenGL坐标与纹理坐标Y轴相反，所以坐标y取反
            1f, 0f, 1f, 1f, 0f, 1f, 0f, 0f
    };
    // 纹理顶点绘制顺序索引缓冲
    private static int BUFFER_TEXTURE_VERTEX;


    private GLSurfaceViewExt mGLSurfaceViewExt;
    private List<GLView> mViews;
    private List<GLAnimationUpdater> mUpdaters;
    private float mWidth;
    private float mHeight;
    private boolean mIsPrepared;

    private GLProgram mProgram;

//    private int[] mBuffers;
//    private int mNextFreeBuffer;

    private float[] mMVPMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];

    public GLAnimationEngine(GLSurfaceViewExt glSurfaceViewExt) {
        mGLSurfaceViewExt = glSurfaceViewExt;
    }

    public void setViews(List<GLView> views) {
        mViews = views;
    }

//    public int pollBuffer() {
//        int buffer = mNextFreeBuffer;
//        ++mNextFreeBuffer;
//        return buffer;
//    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e(TAG, "onSurfaceCreated");

        // compileGLProgram();
        mProgram = new GLProgram(mGLSurfaceViewExt.getContext());
        mProgram.use();

        // face-culling
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        // alpha blend
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // depth testing
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // load textures
        Utils.loadTextures(mViews);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG, "onSurfaceChanged, width=" + width
                + " height=" + height);

        mWidth = width;
        mHeight = height;

        prepare();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mViews == null || mViews.isEmpty()) {
            Log.e(TAG, "onDrawFrame, return by no views");
            return;
        }

        // clear
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT/* | GLES20.GL_DEPTH_BUFFER_BIT*/);// 深度是不变的

        // texture vertex index
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, BUFFER_TEXTURE_VERTEX);
        GLES20.glVertexAttribPointer(mProgram.getAttribute_texture_coord(), 2, GLES20.GL_FLOAT,
                false, 0, 0);
        GLES20.glEnableVertexAttribArray(mProgram.getAttribute_texture_coord());

        for (GLView view : mViews) {
            // texture
            GLTexture texture = view.getTexture();
            if (texture != null && texture.isValid()) {
                GLES20.glActiveTexture(texture.index);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.id);
                GLES20.glUniform1i(mProgram.getUniform_texture(), texture.index - GLES20.GL_TEXTURE0);
            }

            // vertex coord
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, view.getBufferVertexCoord());
            GLES20.glVertexAttribPointer(mProgram.getAttribute_vertex_coord(), 3, GLES20.GL_FLOAT,
                    false, 0, 0);
            GLES20.glEnableVertexAttribArray(mProgram.getAttribute_vertex_coord());

            // mvp matrix
            Matrix.setIdentityM(mViewMatrix, 0);
            Matrix.setIdentityM(mProjectionMatrix, 0);
            float aspectRatio = Math.max(mWidth, mHeight) / Math.min(mWidth, mHeight);
            if (mWidth > mHeight) {
                Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 0f, 1f);
            } else {
                Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 0f, 1f);
            }
            Matrix.setIdentityM(mMVPMatrix, 0);
            Matrix.multiplyMM(mMVPMatrix, 0, view.getModelMatrix(), 0, mMVPMatrix, 0);
            Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mMVPMatrix, 0);
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
            GLES20.glUniformMatrix4fv(mProgram.getUniform_mvp_matrix(), 1, false, mMVPMatrix, 0);

            // multiply color
            GLES20.glUniform4f(mProgram.getUniform_multiply_color(), 1f, 1f, 1f, 1f);

            // view vertex index
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, BUFFER_VERTEX_INDEX);

            // draw elements
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, 0);
        }
    }

    private void prepare() {
        if (mIsPrepared) {
            Log.e(TAG, "prepare, return by already prepared");
            return;
        }

        if (mViews == null || mViews.isEmpty()) {
            Log.e(TAG, "prepare, return by no views");
            return;
        }

        // measure, layout and create updaters
        mUpdaters = new ArrayList<>(mViews.size());
        for (GLView view : mViews) {
            view.onMeasure((int) mWidth, (int) mHeight);
            view.onLayout(0, 0, (int) mWidth, (int) mHeight);
            if (view.getAnimation() != null) {
                GLAnimationUpdater updater = new GLAnimationUpdater(view, view.getAnimation());
                mUpdaters.add(updater);
                Log.e(TAG, "added animation updater, name=" + view.mName);
            }
        }

        // generate buffers
        int bufferCount = 0;
        for (GLView view : mViews) {
            bufferCount += view.getNeedBufferCount();
        }
        if (bufferCount > 0) {
            int publicBufferCount = 2;// BUFFER_VERTEX_INDEX、BUFFER_TEXTURE_VERTEX也各需要一个
            bufferCount += publicBufferCount;
            Log.e(TAG, "bufferCount=" + bufferCount);
            int buffers[] = new int[bufferCount];
            GLES20.glGenBuffers(bufferCount, buffers, 0);

            BUFFER_VERTEX_INDEX = buffers[0];
            ShortBuffer vertexIndexBufferData = Utils.toShortBuffer(ARRAY_VERTEX_INDEX);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, BUFFER_VERTEX_INDEX);
            GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                    ARRAY_VERTEX_INDEX.length * Utils.BYTES_OF_SHORT,
                    vertexIndexBufferData, GLES20.GL_STATIC_DRAW);

            BUFFER_TEXTURE_VERTEX = buffers[1];
            FloatBuffer textureVertexBufferData = Utils.toFloatBuffer(ARRAY_TEXTURE_VERTEX);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, BUFFER_TEXTURE_VERTEX);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                    ARRAY_VERTEX_INDEX.length * Utils.BYTES_OF_FLOAT,
                    textureVertexBufferData, GLES20.GL_STATIC_DRAW);

            int bufferIndex = publicBufferCount;
            for (GLView view : mViews) {
                int used = view.saveBuffers(buffers, bufferIndex);
                bufferIndex += used;
            }
        }

        // viewport
        GLES20.glViewport(0, 0, (int) mWidth, (int) mHeight);

        // prepare animation and delay start
        for (GLAnimationUpdater updater : mUpdaters) {
            updater.prepare((int) mWidth, (int) mHeight);
        }
        mGLSurfaceViewExt.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (GLAnimationUpdater updater : mUpdaters) {
                    updater.start();
                }
            }
        }, 100L);

        mIsPrepared = true;
        Log.e(TAG, "prepared");
    }
}
