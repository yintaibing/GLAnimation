package me.yintaibing.glanimation_test;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class NativeRomanticDateActivity extends Activity {
    String DIR = "sdcard/com.zhenai.android/file/gift/3/";
    String FILE_FLOWER = DIR + "bg_live_video_romantic_date_flower.png";
    String FILE_LEFT_HAND = DIR + "icon_live_video_romantic_date_left_hand.png";
    String FILE_RIGHT_HAND = DIR + "icon_live_video_romantic_date_right_hand.png";

    private ImageView mIvFlower, mIvLeftHand, mIvRightHand;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_romantic_date);

        mIvFlower = findViewById(R.id.iv_flower);
        mIvLeftHand = findViewById(R.id.iv_left_hand);
        mIvRightHand = findViewById(R.id.iv_right_hand);

        mIvFlower.setImageBitmap(BitmapFactory.decodeFile(FILE_FLOWER));
        mIvLeftHand.setImageBitmap(BitmapFactory.decodeFile(FILE_LEFT_HAND));
        mIvRightHand.setImageBitmap(BitmapFactory.decodeFile(FILE_RIGHT_HAND));

        mIvFlower.postDelayed(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation f = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_PARENT, 1f, Animation.RELATIVE_TO_SELF, 0f);
                f.setDuration(2000);
                mIvFlower.startAnimation(f);

                TranslateAnimation l = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                l.setDuration(2000);
                mIvLeftHand.startAnimation(l);

                TranslateAnimation r = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                r.setDuration(2000);
                mIvRightHand.startAnimation(r);
            }
        }, 1000L);
    }
}
