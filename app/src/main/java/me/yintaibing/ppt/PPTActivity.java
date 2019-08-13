package me.yintaibing.ppt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.yintaibing.glanimation.Utils;
import me.yintaibing.glanimation_test.R;

public class PPTActivity extends Activity {
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppt);

        glSurfaceView = findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(
                8, 8, 8, 8, // 红，绿，蓝，alpha
                16, // 深度缓冲
                0);// 模板缓冲e
        glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glSurfaceView.setRenderer(new SimpleShapeRenderer());
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
// GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    Context getContext() {
        return this;
    }

    class SimpleShapeRenderer implements GLSurfaceView.Renderer {
        int mProgramHandle;

        float[] aPositionArray = {
                0.5f, 0.5f, 0f,//右上
                -0.5f, 0.5f, 0f,//左上
                -0.5f, -0.5f, 0f,//左下

//                -0.5f, -0.5f, 0f,//左下
                0.5f, -0.5f, 0f,//右下
//                0.5f, 0.5f, 0f,//右上
        };
        int aPositionBuffer;
        int aPositionLocation;

        float[] aColorArray = {
                1f, 0f, 0f, 1f,//右上
                0f, 1f, 0f, 1f,//左上
                0f, 0f, 1f, 1f,//左下
                1f, 1f, 1f, 1f,//右下
        };
        int aColorBuffer;
        int aColorLocation;

        int textureId;
        float[] aTexPositionArray = {
                1f, 0f,
                0f, 0f,
                0f, 1f,
                1f, 1f
        };
        int aTexPositionBuffer;
        int aTexPositionLocation;
        int uTextureLocation;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            String vertexShaderSrcCode = Utils.readRaw(getContext(), R.raw.simple_vertex_shader);
            String fragmentShaderSrcCode = Utils.readRaw(getContext(), R.raw.simple_fragment_shader);
            int vertexShaderId = Utils.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSrcCode);
            int fragmentShaderId = Utils.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSrcCode);
            mProgramHandle = Utils.linkProgram(vertexShaderId, fragmentShaderId);
            aPositionLocation = GLES20.glGetAttribLocation(mProgramHandle, "aPosition");
            aColorLocation = GLES20.glGetAttribLocation(mProgramHandle, "aColor");
            aTexPositionLocation = GLES20.glGetAttribLocation(mProgramHandle, "aTexPosition");
            uTextureLocation = GLES20.glGetUniformLocation(mProgramHandle, "uTex");

            int[] buffers = new int[3];
            GLES20.glGenBuffers(buffers.length, buffers, 0);
            aPositionBuffer = buffers[0];
            FloatBuffer aPositionBuffer = Utils.toFloatBuffer(aPositionArray);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.aPositionBuffer);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, aPositionArray.length * Utils.BYTES_OF_FLOAT,
                    aPositionBuffer, GLES20.GL_STATIC_DRAW);

            aColorBuffer = buffers[1];
            FloatBuffer aColorFloatBuffer = Utils.toFloatBuffer(aColorArray);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, aColorBuffer);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, aColorArray.length * Utils.BYTES_OF_FLOAT,
                    aColorFloatBuffer, GLES20.GL_STATIC_DRAW);

            aTexPositionBuffer = buffers[2];
            FloatBuffer aTexturePositionBuffer = Utils.toFloatBuffer(aTexPositionArray);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, aTexPositionBuffer);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, aTexPositionArray.length * Utils.BYTES_OF_FLOAT,
                    aTexturePositionBuffer, GLES20.GL_STATIC_DRAW);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.texture);
            int[] textureIds = new int[1];
            textureId = textureIds[0];
            GLES20.glGenTextures(1, textureIds, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClearColor(0.5f, 0.5f, 1f, 1f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            GLES20.glUseProgram(mProgramHandle);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, aPositionBuffer);
            GLES20.glVertexAttribPointer(aPositionLocation, 3, GLES20.GL_FLOAT, false, 0, 0);
            GLES20.glEnableVertexAttribArray(aPositionLocation);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, aColorBuffer);
            GLES20.glVertexAttribPointer(aColorLocation, 4, GLES20.GL_FLOAT, false, 0, 0);
            GLES20.glEnableVertexAttribArray(aColorLocation);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, aTexPositionBuffer);
            GLES20.glVertexAttribPointer(aTexPositionLocation, 2, GLES20.GL_FLOAT, false, 0, 0);
            GLES20.glEnableVertexAttribArray(aTexPositionLocation);

            GLES20.glActiveTexture(textureId);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(uTextureLocation, textureId);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

            GLES20.glDisableVertexAttribArray(aPositionLocation);
            GLES20.glDisableVertexAttribArray(aTexPositionLocation);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glUseProgram(0);
        }
    }
}
