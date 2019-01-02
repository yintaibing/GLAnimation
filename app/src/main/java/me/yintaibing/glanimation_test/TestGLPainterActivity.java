package me.yintaibing.glanimation_test;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.yintaibing.glanimation.GLAnimation;
import me.yintaibing.glanimation_test.R;

public class TestGLPainterActivity extends Activity {
    private GLSurfaceView mGLSurfaceView;
    private TestGLPainter testGLPainter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);


        mGLSurfaceView = findViewById(R.id.gl_surface_view);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            private boolean mFirst = true;
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                Log.e("###", "onSurfaceCreated");

                testGLPainter.prepare(TestGLPainterActivity.this);
                GLES20.glClearColor(1f, 0f, 0f, 1f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

//                startDrawThread();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startUpdateAnimator();
                    }
                });
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                Log.e("###", "onSurfaceChanged");

                GLES20.glViewport(0, 0, width, height);
                testGLPainter.width = width;
                testGLPainter.height = height;
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                if (mFirst) {
                    Log.e("###", "onDrawFrame");
                    mFirst = false;
                }
                testGLPainter.draw();
            }
        });
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        testGLPainter = new TestGLPainter();
    }

    private void startDrawThread() {
        new Thread() {
            @Override
            public void run() {
                float fromScale = 1f;
                float toScale = 0f;
                long duration = 2000L;
                long frameInternal = 100L;
                long frameCount = duration / frameInternal;
                float step = (toScale - fromScale) / frameCount;

                float currentScale = fromScale;
                boolean run = true;
                do {
                    testGLPainter.scale(currentScale);
                    mGLSurfaceView.requestRender();
                    currentScale += step;
                    if (currentScale < toScale) {
                        run = false;
                    }

                    try {
                        Thread.sleep(frameInternal);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (run);
            }
        }.start();
    }

    private void startUpdateAnimator() {
        ValueAnimator a = ValueAnimator.ofFloat(1, 0);
        a.setDuration(5000L);
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                testGLPainter.scale(progress);
//                testGLPainter.translate(progress);
            }
        });
        a.start();
    }
}
