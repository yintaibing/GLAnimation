package me.yintaibing.glanimation_test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;

import me.yintaibing.glanimation.GLAnimation;
import me.yintaibing.glanimation.GLRotateAnimation;
import me.yintaibing.glanimation.GLSurfaceViewExt;
import me.yintaibing.glanimation.GLTexture;
import me.yintaibing.glanimation.GLTranslateAnimation;
import me.yintaibing.glanimation.GLView;

public class GLRomanticDateActivity extends Activity {
    String DIR = "sdcard/com.zhenai.android/file/gift/3/";
    String FILE_FLOWER = DIR + "bg_live_video_romantic_date_flower.png";
    String FILE_FLOWER_ETC = DIR + "bg_live_video_romantic_date_flower.pkm";
    String FILE_LEFT_HAND = DIR + "icon_live_video_romantic_date_left_hand.png";
    String FILE_RIGHT_HAND = DIR + "icon_live_video_romantic_date_right_hand.png";

    GLSurfaceViewExt glSurfaceViewExt;
    private List<GLView> mGLViews;

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
        mGLViews = new ArrayList<>();

//        addFlower();
        addLeftHand();
//        addRightHand();

        glSurfaceViewExt.render(mGLViews);
    }

    private void addFlower() {
        GLView flower = new GLView("flower");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.widthRatio = 1.0f;
        lp.heightRatio = 0.5f;
        lp.z = 0.2f;
        lp.gravity = Gravity.BOTTOM;
        flower.setLayoutParams(lp);

        GLTexture texture = new GLTexture(FILE_FLOWER);
        flower.setTexture(texture);

        GLAnimation animation = new GLTranslateAnimation(
                GLAnimation.RELATIVE_TO_SELF, 0f, GLAnimation.RELATIVE_TO_SELF, 0f,
                GLAnimation.RELATIVE_TO_SELF, -1f, GLAnimation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(2000L);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        flower.setAnimation(animation);

        mGLViews.add(flower);
    }

    private void addLeftHand() {
        GLView leftHand = new GLView("leftHand");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.width = 400;
        lp.height = 200;
        lp.z = 0.5f;
        lp.gravity = Gravity.CENTER_VERTICAL;
        leftHand.setLayoutParams(lp);

        GLTexture texture = new GLTexture(FILE_LEFT_HAND);
        leftHand.setTexture(texture);

//        GLAnimation animation = new GLTranslateAnimation(
//                GLAnimation.RELATIVE_TO_SELF, -1f, GLAnimation.RELATIVE_TO_SELF, 0f,
//                GLAnimation.RELATIVE_TO_SELF, 0f, GLAnimation.RELATIVE_TO_SELF, 0f);
        GLAnimation animation = new GLRotateAnimation(0f, 360f,
                GLAnimation.RELATIVE_TO_SELF, 0.5f,
                GLAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(4000L);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        leftHand.setAnimation(animation);

        mGLViews.add(leftHand);
    }

    private void addRightHand() {
        GLView rightHand = new GLView("rightHand");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.width = 400;
        lp.height = 400;
        lp.z = 0.8f;
        lp.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        rightHand.setLayoutParams(lp);

        GLTexture texture = new GLTexture(FILE_RIGHT_HAND);
        rightHand.setTexture(texture);

        GLAnimation animation = new GLTranslateAnimation(
                GLAnimation.RELATIVE_TO_SELF, 1f, GLAnimation.RELATIVE_TO_SELF, 0f,
                GLAnimation.RELATIVE_TO_SELF, 0f, GLAnimation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(2000L);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        rightHand.setAnimation(animation);

        mGLViews.add(rightHand);
    }
}
