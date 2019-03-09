package me.yintaibing.glanimation_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 浪漫之约礼物控件
 *
 * @author yintaibing
 * @date 2017/5/27
 */
public class RomanticDateWidget extends FrameLayout {
    String DIR              = "sdcard/com.zhenai.android/file/gift/3/";

    private ImageView mIvShadow;// 灰暗背景
    private ImageView mIvHeartLight;// 粉色爱心背景
    private ImageView mIvHeartLightBlink1;// 粉色爱心闪光
    private ImageView mIvHeartLightBlink2;
    private FrameLayout mLayoutFlower;// 底部花丛
    private ImageView mIvFlowerLight2;// 底部花丛上的闪光
    private ImageView mIvFlowerLight3;
    private ImageView mIvLeftHand;// 左手
    private ImageView mIvRightHand;// 右手
    private ImageView mIvFloatHeart;// 向上飘的爱心
    private FrameLayout mLayoutFalling;// 落花
    private ImageView mIvFallingRotate;// 落花+旋转

    private Runnable mFlowerLightTask;

    private Animation mAnimationShadow;
    private Animation mAnimationHeartLight;
    private Animation mAnimationHeartLightBlink1;
    private Animation mAnimationHeartLightBlink2;
    private Animation mAnimationFlower;
    private Animation mAnimationFlowerLight2;
    private Animation mAnimationFlowerLight3;
    private Animation mAnimationLeftHand;
    private Animation mAnimationRightHand;
    private Animation mAnimationFloatHeart;
    private Animation mAnimationFalling;
    private Animation mAnimationFallingRotate;

    private List<Bitmap> mBitmaps;

    public RomanticDateWidget(Context context) {
        this(context, null, 0);
    }

    public RomanticDateWidget(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RomanticDateWidget(Context context, AttributeSet attributeSet, int style) {
        super(context, attributeSet, style);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mIvShadow = (ImageView) findViewById(R.id.iv_live_video_romantic_date_shadow);
        mIvHeartLight = (ImageView) findViewById(R.id.iv_live_video_romantic_date_heart_light);
        mIvHeartLightBlink1 = (ImageView) findViewById(R.id.iv_live_video_romantic_date_heart_light_blink_1);
        mIvHeartLightBlink2 = (ImageView) findViewById(R.id.iv_live_video_romantic_date_heart_light_blink_2);
        mLayoutFlower = (FrameLayout) findViewById(R.id.layout_live_video_romantic_date_flower);
        mIvFlowerLight2 = (ImageView) findViewById(R.id.iv_live_video_romantic_date_flower_light_2);
        mIvFlowerLight3 = (ImageView) findViewById(R.id.iv_live_video_romantic_date_flower_light_3);
        mIvLeftHand = (ImageView) findViewById(R.id.iv_live_video_romantic_date_left_hand);
        mIvRightHand = (ImageView) findViewById(R.id.iv_live_video_romantic_date_right_hand);
        mIvFloatHeart = (ImageView) findViewById(R.id.iv_live_video_romantic_date_float_heart);
        mLayoutFalling = (FrameLayout) findViewById(R.id.layout_live_video_romantic_date_falling);
        mIvFallingRotate = (ImageView) findViewById(R.id.iv_live_video_romantic_date_falling_rotate);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mFlowerLightTask);
    }

    public void showEffect() {
        String[] files = {
                "bg_live_video_romantic_date_heart_light.png",          // 0
                "icon_live_video_romantic_date_heart_light_blink_1.png",// 1
                "icon_live_video_romantic_date_heart_light_blink_2.png",// 2
                "bg_live_video_romantic_date_flower.png",               // 3
                "icon_live_video_romantic_date_flower_light_1.png",     // 4
                "icon_live_video_romantic_date_flower_light_2.png",     // 5
                "icon_live_video_romantic_date_flower_light_3.png",     // 6
                "icon_live_video_romantic_date_left_hand.png",          // 7
                "icon_live_video_romantic_date_right_hand.png",         // 8
                "icon_live_video_romantic_date_float_heart.png",        // 9
                "bg_live_video_romantic_date_falling.png",              // 10
                "icon_live_video_romantic_date_falling_rotate.png"      // 11
        };
        mBitmaps = new ArrayList<>(files.length);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inDensity = DisplayMetrics.DENSITY_XHIGH;
        opt.inTargetDensity = getResources().getDisplayMetrics().densityDpi;
        opt.inScaled = true;
        long byteCount = 0L;
        long allocationByteCount = 0L;
        for (String file : files) {
            Bitmap bitmap = BitmapFactory.decodeFile(DIR + file, opt);
            mBitmaps.add(bitmap);
            byteCount += bitmap.getByteCount();
            if (Build.VERSION.SDK_INT >= 19) {
                allocationByteCount += bitmap.getAllocationByteCount();
            }
        }
        Log.e("RomanticDate", "byteCount=" + byteCount + " allocationByteCount=" + allocationByteCount);
//                }, DisplayMetrics.DENSITY_XHIGH, false);// 原图是按DENSITY_XHIGH制作的
        mIvHeartLight.setImageBitmap(mBitmaps.get(0));
        mIvHeartLightBlink1.setImageBitmap(mBitmaps.get(1));
        mIvHeartLightBlink2.setImageBitmap(mBitmaps.get(2));
        ((ImageView) mLayoutFlower.getChildAt(0)).setImageBitmap(mBitmaps.get(3));
        ((ImageView) mLayoutFlower.getChildAt(1)).setImageBitmap(mBitmaps.get(4));
        mIvFlowerLight2.setImageBitmap(mBitmaps.get(5));
        mIvFlowerLight3.setImageBitmap(mBitmaps.get(6));
        mIvLeftHand.setImageBitmap(mBitmaps.get(7));
        mIvRightHand.setImageBitmap(mBitmaps.get(8));
        mIvFloatHeart.setImageBitmap(mBitmaps.get(9));
        ((ImageView) mLayoutFalling.getChildAt(0)).setImageBitmap(mBitmaps.get(10));
        mIvFallingRotate.setImageBitmap(mBitmaps.get(11));

        init();

        mIvShadow.startAnimation(mAnimationShadow);
        mIvHeartLight.startAnimation(mAnimationHeartLight);
        mIvHeartLightBlink1.startAnimation(mAnimationHeartLightBlink1);
        mIvHeartLightBlink2.startAnimation(mAnimationHeartLightBlink2);
        mLayoutFlower.startAnimation(mAnimationFlower);
        postDelayed(mFlowerLightTask = new Runnable() {
            @Override
            public void run() {
                mIvFlowerLight2.startAnimation(mAnimationFlowerLight2);
                mIvFlowerLight3.startAnimation(mAnimationFlowerLight3);
            }
        }, 1000L);
        mIvLeftHand.startAnimation(mAnimationLeftHand);
        mIvRightHand.startAnimation(mAnimationRightHand);
        mIvFloatHeart.startAnimation(mAnimationFloatHeart);
        mLayoutFalling.startAnimation(mAnimationFalling);
//        mIvFallingRotate.startAnimation(mAnimationFallingRotate);
    }

    private void init() {
        // 灰暗背景动画
        mAnimationShadow = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_shadow);
        EffectUnitAnimationListener.goneOnEnd(mIvShadow, mAnimationShadow);

        // 粉色爱心背景动画
        mAnimationHeartLight = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_heart_light);
        EffectUnitAnimationListener.goneOnEnd(mIvHeartLight, mAnimationHeartLight);

        // 粉色爱心闪光动画
        mAnimationHeartLightBlink1 = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_flower_light_2);
        mAnimationHeartLightBlink1.setRepeatMode(Animation.REVERSE);
        mAnimationHeartLightBlink1.setRepeatCount(6);
        EffectUnitAnimationListener.goneOnEnd(mIvHeartLightBlink1, mAnimationHeartLightBlink1);

        mAnimationHeartLightBlink2 = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_flower_light_3);
        mAnimationHeartLightBlink2.setRepeatMode(Animation.REVERSE);
        mAnimationHeartLightBlink2.setRepeatCount(6);
        EffectUnitAnimationListener.goneOnEnd(mIvHeartLightBlink2, mAnimationHeartLightBlink2);

        // 底部花丛动画
        mAnimationFlower = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_flower);
        EffectUnitAnimationListener.goneOnEnd(mLayoutFlower, mAnimationFlower);

        // 底部花丛动画闪光动画
        mAnimationFlowerLight2 = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_flower_light_2);
        mAnimationFlowerLight2.setRepeatMode(Animation.REVERSE);
        mAnimationFlowerLight2.setRepeatCount(3);
        EffectUnitAnimationListener.goneOnEnd(mIvFlowerLight2, mAnimationFlowerLight2);

        mAnimationFlowerLight3 = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_flower_light_3);
        mAnimationFlowerLight3.setRepeatMode(Animation.REVERSE);
        mAnimationFlowerLight3.setRepeatCount(3);
        EffectUnitAnimationListener.goneOnEnd(mIvFlowerLight3, mAnimationFlowerLight3);

        // 左手动画
        mAnimationLeftHand = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_left_hand);
        EffectUnitAnimationListener.goneOnEnd(mIvLeftHand, mAnimationLeftHand);

        // 右手动画
        mAnimationRightHand = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_right_hand);
        EffectUnitAnimationListener.goneOnEnd(mIvRightHand, mAnimationRightHand);

        // 向上飘的爱心动画
        mAnimationFloatHeart = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_float_heart);
        EffectUnitAnimationListener.goneOnEnd(mIvFloatHeart, mAnimationFloatHeart);

        // 落花动画
        mAnimationFalling = AnimationUtils.loadAnimation(
                getContext(), R.anim.anim_live_video_romantic_date_falling);
        EffectUnitAnimationListener.goneOnEnd(mLayoutFalling, mAnimationFalling);

        // 落花+旋转动画
//        mAnimationFallingRotate = AnimationUtils.loadAnimation(
//                getContext(), R.anim.anim_live_video_romantic_date_falling_rotate);
    }
}
