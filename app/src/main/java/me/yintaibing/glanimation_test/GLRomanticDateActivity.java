package me.yintaibing.glanimation_test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;

import me.yintaibing.glanimation.GLAnimation;
import me.yintaibing.glanimation.GLSurfaceViewExt;
import me.yintaibing.glanimation.GLTexture;
import me.yintaibing.glanimation.GLTranslateAnimation;
import me.yintaibing.glanimation.GLView;

public class GLRomanticDateActivity extends Activity {
    String DIR = "sdcard/com.zhenai.android/file/gift/3/";
    String FILE_FLOWER = DIR + "bg_live_video_romantic_date_flower.png";
    String FILE_LEFT_HAND = DIR + "icon_live_video_romantic_date_left_hand.png";
    String FILE_RIGHT_HAND = DIR + "icon_live_video_romantic_date_right_hand.png";

    GLSurfaceViewExt glSurfaceViewExt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl_romantic_date);

        glSurfaceViewExt = findViewById(R.id.gl_surface_view);
        glSurfaceViewExt.postDelayed(new Runnable() {
            @Override
            public void run() {
                romanticDate();
            }
        }, 1000L);
    }

    private void romanticDate() {
        List<GLView> views = new ArrayList<>();

        addFlower(views);

        glSurfaceViewExt.render(views);
    }

    private void addFlower(List<GLView> views) {
        GLView flower = new GLView("flower");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.widthRatio = 0.25f;
        lp.heightRatio = 0.25f;
//        lp.z = 0.5f;
        lp.gravity = Gravity.BOTTOM;
        flower.setLayoutParams(lp);

        GLTexture texture = new GLTexture(FILE_FLOWER);
        flower.setTexture(texture);

        GLAnimation animation = new GLTranslateAnimation(
                GLAnimation.RELATIVE_TO_PARENT, 1f, GLAnimation.RELATIVE_TO_PARENT, 1f,
                GLAnimation.RELATIVE_TO_PARENT, 0f, GLAnimation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(2000L);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        flower.setAnimation(animation);

        views.add(flower);
    }
}
