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

    String FILE_HEART_LIGHT     = DIR + "bg_live_video_romantic_date_heart_light.png";
    String FILE_HEART_BLINK_1   = DIR + "icon_live_video_romantic_date_heart_light_blink_1.png";
    String FILE_HEART_BLINK_2   = DIR + "icon_live_video_romantic_date_heart_light_blink_2.png";
    String FILE_FLOWER          = DIR + "bg_live_video_romantic_date_flower.png";
    String FILE_FLOWER_LIGHT_1  = DIR + "icon_live_video_romantic_date_flower_light_1.png";
    String FILE_FLOWER_LIGHT_2  = DIR + "icon_live_video_romantic_date_flower_light_2.png";
    String FILE_FLOWER_LIGHT_3  = DIR + "icon_live_video_romantic_date_flower_light_3.png";
    String FILE_LEFT_HAND       = DIR + "icon_live_video_romantic_date_left_hand.png";
    String FILE_RIGHT_HAND      = DIR + "icon_live_video_romantic_date_right_hand.png";
    String FILE_FLOAT_HEART     = DIR + "icon_live_video_romantic_date_float_heart.png";
    String FILE_FALLING         = DIR + "bg_live_video_romantic_date_falling.png";

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

        addHeartLight();
        addHeartBlinks();
        addFlower();
        addFlowerLights();
        addLeftHand();
        addRightHand();
        addFloatHeart();
        addFalling();

        glSurfaceViewExt.render(mGLViews);
    }

    private void addHeartLight() {
        GLView heartLight = new GLView("heartLight");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.widthRatio = 1.0f;
        lp.heightRatio = 0.5f;
        lp.z = 0.1f;
        lp.gravity = Gravity.CENTER;
        heartLight.setLayoutParams(lp);

        heartLight.setTexture(etc1(FILE_HEART_LIGHT));

        mGLViews.add(heartLight);
    }

    private void addHeartBlinks() {
        for (int i = 0; i < 2; i++) {
            GLView blink = new GLView(i == 0 ? "heartBlink1" : "heartBlink2");

            GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
            lp.widthRatio = 1.0f;
            lp.heightRatio = 0.5f;
            lp.z = 0.2f;
            lp.gravity = Gravity.CENTER;
            blink.setLayoutParams(lp);

            blink.setTexture(etc1(i == 0 ? FILE_HEART_BLINK_1 : FILE_HEART_BLINK_2));

            mGLViews.add(blink);
        }
    }

    private void addFlower() {
        GLView flower = new GLView("flower");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.widthRatio = 1.0f;
        lp.heightRatio = 0.5f;
        lp.z = 0.3f;
        lp.gravity = Gravity.BOTTOM;
        flower.setLayoutParams(lp);

        flower.setTexture(etc1(FILE_FLOWER));

        GLAnimation animation = new GLTranslateAnimation(
                GLAnimation.RELATIVE_TO_SELF, 0f, GLAnimation.RELATIVE_TO_SELF, 0f,
                GLAnimation.RELATIVE_TO_SELF, -1f, GLAnimation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(2000L);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        flower.setAnimation(animation);

        mGLViews.add(flower);
    }

    private void addFlowerLights() {
        String[] files = {FILE_FLOWER_LIGHT_1, FILE_FLOWER_LIGHT_2, FILE_FLOWER_LIGHT_3};
        for (int i = 0; i < 3; i++) {
            GLView light = new GLView("flowerLight" + i);

            GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
            lp.widthRatio = 1.0f;
            lp.heightRatio = 0.5f;
            lp.z = 0.4f;
            lp.gravity = Gravity.CENTER;
            light.setLayoutParams(lp);

            light.setTexture(etc1(files[i]));

            mGLViews.add(light);
        }
    }

    private void addLeftHand() {
        GLView leftHand = new GLView("leftHand");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.widthRatio = 0.5f;
        lp.height = 600;
        lp.z = 0.5f;
        lp.gravity = Gravity.CENTER_VERTICAL;
        leftHand.setLayoutParams(lp);

        leftHand.setTexture(etc1(FILE_LEFT_HAND));

        GLAnimation animation = new GLTranslateAnimation(
                GLAnimation.RELATIVE_TO_SELF, -1f, GLAnimation.RELATIVE_TO_SELF, 0f,
                GLAnimation.RELATIVE_TO_SELF, 0f, GLAnimation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(2000L);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        leftHand.setAnimation(animation);

        mGLViews.add(leftHand);
    }

    private void addRightHand() {
        GLView rightHand = new GLView("rightHand");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.widthRatio = 0.5f;
        lp.height = 600;
        lp.z = 0.5f;
        lp.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        rightHand.setLayoutParams(lp);

        rightHand.setTexture(etc1(FILE_RIGHT_HAND));

        GLAnimation animation = new GLTranslateAnimation(
                GLAnimation.RELATIVE_TO_SELF, 1f, GLAnimation.RELATIVE_TO_SELF, 0f,
                GLAnimation.RELATIVE_TO_SELF, 0f, GLAnimation.RELATIVE_TO_SELF, 0f);
//        GLAnimation animation = new GLAlphaAnimation(0f, 1f);
        animation.setDuration(2000L);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        rightHand.setAnimation(animation);

        mGLViews.add(rightHand);
    }

    private void addFloatHeart() {
        GLView floatHeart = new GLView("floatHeart");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.widthRatio = 0.5f;
        lp.height = 600;
        lp.z = 0.6f;
        lp.gravity = Gravity.CENTER;
        floatHeart.setLayoutParams(lp);

        floatHeart.setTexture(etc1(FILE_FLOAT_HEART));

        GLAnimation animation = new GLTranslateAnimation(
                GLAnimation.RELATIVE_TO_SELF, 0f, GLAnimation.RELATIVE_TO_SELF, 0f,
                GLAnimation.RELATIVE_TO_SELF, 0f, GLAnimation.RELATIVE_TO_PARENT, 0f);
        animation.setDuration(2000L);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        floatHeart.setAnimation(animation);

        mGLViews.add(floatHeart);
    }

    private void addFalling() {
        GLView falling = new GLView("falling");

        GLView.GLLayoutParams lp = new GLView.GLLayoutParams();
        lp.widthRatio = 1.0f;
        lp.heightRatio = 1.0f;
        lp.z = 0.7f;
        falling.setLayoutParams(lp);

        falling.setTexture(etc1(FILE_FALLING));

        GLAnimation animation = new GLTranslateAnimation(
                GLAnimation.RELATIVE_TO_SELF, 0f, GLAnimation.RELATIVE_TO_SELF, 0f,
                GLAnimation.RELATIVE_TO_PARENT, -1f, GLAnimation.RELATIVE_TO_PARENT, 0f);
        animation.setDuration(5000L);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        falling.setAnimation(animation);

        mGLViews.add(falling);
    }

    private GLTexture etc1(String pngFile) {
        String name = pngFile.substring(0, pngFile.length() - 4);
        String etc1 = name + ".pkm";
        String etc1Alpha = name + "_alpha.pkm";
        return new GLTexture(etc1, etc1Alpha);
    }
}
