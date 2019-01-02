package me.yintaibing.glanimation;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

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
        setEGLConfigChooser(8, 8, 8, 8, 24, 0);
        setZOrderOnTop(false);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        getHolder().addCallback(null);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setRenderer(mEngine);
    }
}
