package me.yintaibing.glanimation_test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.yintaibing.glanimation.GLSurfaceViewExt;
import me.yintaibing.glanimation.GLTexture;
import me.yintaibing.glanimation.GLView;

public class GLCastleActivity extends Activity {
    String DIR = "sdcard/com.zhenai.android/file/gift/28/";

    String FILE_FIREWORK = DIR + "live_video_1314_%s_firework_%d.png";
    String FILE_LIGHT = DIR + "live_video_1314_light.png";
    String FILE_1314 = DIR + "live_video_1314_%d.png";
    String FILE_CASTLE = DIR + "img_%d.png";

    GLSurfaceViewExt glSurfaceViewExt;
    List<GLView> mGLViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl_castle);

        glSurfaceViewExt = findViewById(R.id.gl_surface_view);
        glSurfaceViewExt.postDelayed(new Runnable() {
            @Override
            public void run() {
                castle();
            }
        }, 1000L);
    }

    private void castle() {
        mGLViews = new ArrayList<>();

        addFireworks();
        addLight();
        add1314();
        addCastle();

        glSurfaceViewExt.render(mGLViews);
    }

    private void addFireworks() {
        int smallCount = 12;
        int bigCount = 19;
        Random random = new Random();
        int small = 1, big = 1;
        do {
            GLView view = new GLView("smallFirework" + small);
            GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
            lp.width = 100;
            lp.height = 100;
            lp.leftMargin = lp.topMargin = random.nextInt(100);
            lp.z = 0.1f;
            view.setLayoutParams(lp);
            String pkm = String.format(FILE_FIREWORK, "small", small);
            view.setTexture(etc1(pkm));
            mGLViews.add(view);
            small++;
        } while (small <= smallCount);
        do {
            GLView view = new GLView("bigFirework" + big);
            GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
            lp.width = 150;
            lp.height = 150;
            lp.leftMargin = lp.topMargin = random.nextInt(150);
            lp.z = 0.2f;
            view.setLayoutParams(lp);
            String pkm = String.format(FILE_FIREWORK, "big", big);
            view.setTexture(etc1(pkm));
            mGLViews.add(view);
            big++;
        } while (big <= bigCount);
    }

    private void addLight() {
        GLView view = new GLView("light");
        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.width = 350;
        lp.height = 350;
        lp.gravity = Gravity.CENTER;
        lp.z = 0.3f;
        view.setLayoutParams(lp);
        view.setTexture(etc1(FILE_LIGHT));
        mGLViews.add(view);
    }

    private void add1314() {
        int lightCount = 2;
        int index = 1;
        do {
            GLView view = new GLView("1314_");
            GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
            lp.width = 500;
            lp.height = 200;
            lp.topMargin = 200 * index;
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            lp.z = 0.4f;
            view.setLayoutParams(lp);
            String pkm = String.format(FILE_1314, index);
            view.setTexture(etc1(pkm));
            mGLViews.add(view);
            index++;
        } while (index <= lightCount);
    }

    private void addCastle() {
        int castleCount = 6;
        int index = 0;
        do {
            GLView view = new GLView("castle_" + index);
            GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
            lp.widthRatio = 1f;
            lp.heightRatio = 0.5f;
            lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            lp.z = 0.5f;
            view.setLayoutParams(lp);
            String pkm = String.format(FILE_CASTLE, index);
            view.setTexture(etc1(pkm));
            mGLViews.add(view);
            index++;
        } while (index <= castleCount);
    }

    private GLTexture etc1(String pngFile) {
        String name = pngFile.substring(0, pngFile.length() - 4);
        String etc1 = name + ".pkm";
        String etc1Alpha = name + "_alpha.pkm";
        return new GLTexture(etc1, etc1Alpha);
    }
}
