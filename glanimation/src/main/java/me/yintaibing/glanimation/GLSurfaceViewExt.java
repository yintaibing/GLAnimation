package me.yintaibing.glanimation;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.util.List;

public class GLSurfaceViewExt extends GLSurfaceView {
    private GLAnimationEngine mEngine;

    public GLSurfaceViewExt(Context context) {
        this(context, null);
    }

    public GLSurfaceViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 0, 16, 0);
        setZOrderOnTop(false);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        getHolder().addCallback(null);
        mEngine = new GLAnimationEngine(this);
        setRenderer(mEngine);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        onPause();
    }

    public void render(List<GLView> views) {
        mEngine.setViews(views);
        onResume();
    }
}
