package me.yintaibing.glanimation_test;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.OnCompositionLoadedListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 城堡1314礼物控件
 *
 * @author yintaibing
 * @date 2017/11/29
 */

public class Castle1314Widget extends FrameAnimatorLayout {
    private static final int SMALL_FIREWORK_FRAME_COUNT = 12;
    private static final int BIG_FIREWORK_FRAME_COUNT = 19;

    private LottieAnimationView mIvCastleLottie;
    private ImageView mIvLight1;
    private ImageView mIvLight2;
    private ImageView mIvBigFirework;
    private ImageView mIv1314_1;
    private ImageView mIv1314_2;

    private List<Bitmap> mBitmaps1314;
    private List<Bitmap> mBitmapsSmallFirework;
    private List<Bitmap> mBitmapsBigFirework;
    private Bitmap mBitmapLight;

    private float mDensity;

    public Castle1314Widget(Context context) {
        this(context, null, 0);
    }

    public Castle1314Widget(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public Castle1314Widget(Context context, AttributeSet attributeSet, int style) {
        super(context, attributeSet, style);

        mDensity = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        mBigGiftQueue = null;
//        mGiftEffectParams = null;
//        BitmapUtils.recycle(mBitmaps1314);
//        BitmapUtils.recycle(mBitmapsSmallFirework);
//        BitmapUtils.recycle(mBitmapsBigFirework);
//        BitmapUtils.recycle(mBitmapLight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mIvCastleLottie = (LottieAnimationView) findViewById(R.id.iv_live_video_castle_lottie);
        mIvLight1 = (ImageView) findViewById(R.id.iv_live_video_castle_light_1);
        mIvLight2 = (ImageView) findViewById(R.id.iv_live_video_castle_light_2);
        mIvBigFirework = (ImageView) findViewById(R.id.iv_live_video_castle_big_firework);
        mIv1314_1 = (ImageView) findViewById(R.id.iv_live_video_castle_1314_1);
        mIv1314_2 = (ImageView) findViewById(R.id.iv_live_video_castle_1314_2);
    }

    public void showEffect() {
        mBitmapLight = LiveResourceManager.getGiftBitmap(getContext(), 28,
                "live_video_1314_light.png", DisplayMetrics.DENSITY_XHIGH);
        mIvLight1.setImageBitmap(mBitmapLight);
        mIvLight2.setImageBitmap(mBitmapLight);

        Context context = getContext();
        int screenWidth = DensityUtils.getScreenWidth(context);
        int screenHeight = DensityUtils.getScreenHeight(context);

        loadBitmaps();
        updateChildrenPosition(screenWidth, screenHeight);
        doCastleLottie();
        doLightAnimation();
        doSmallFireworkAnimation(screenWidth, screenHeight);
        doBigFireworkAnimation();
        do1314Animation();
    }

    private void updateChildrenPosition(int screenWidth, int screenHeight) {
        LayoutParams lp;

        lp = (LayoutParams) mIvBigFirework.getLayoutParams();
        lp.topMargin = (int) (screenHeight * 0.11f);
        float density720 = 2f;
        if (mDensity > density720) {
            lp.topMargin = (int) (lp.topMargin / mDensity);
        }
        mIvBigFirework.setLayoutParams(lp);

        lp = (LayoutParams) mIv1314_1.getLayoutParams();
        lp.topMargin = (int) (screenHeight * 0.34f);
        mIv1314_1.setLayoutParams(lp);

        lp = (LayoutParams) mIv1314_2.getLayoutParams();
        lp.topMargin = (int) (screenHeight * 0.34f);
        mIv1314_2.setLayoutParams(lp);
    }

    private void doLightAnimation() {
        long duration = 3000L;
        RotateAnimation rotate1 = new RotateAnimation(30f, -30f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.8f);
        rotate1.setDuration(duration);
        rotate1.setRepeatMode(Animation.REVERSE);
        rotate1.setRepeatCount(Animation.INFINITE);

        RotateAnimation rotate2 = new RotateAnimation(-30f, 30f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.8f);
        rotate2.setDuration(duration);
        rotate2.setRepeatMode(Animation.REVERSE);
        rotate2.setRepeatCount(Animation.INFINITE);

        mIvLight1.setVisibility(VISIBLE);
        mIvLight2.setVisibility(VISIBLE);
        mIvLight1.startAnimation(rotate1);
        mIvLight2.startAnimation(rotate2);
    }

    private void doSmallFireworkAnimation(int screenWidth, int screenHeight) {
        List<FrameParams> fps = GiftUtils.loadFrameParamJsonAsset(getContext(),
                "live_video_castle_fireworks.json");
        if (fps != null) {
            FrameAnimationTask task;
            for (FrameParams fp : fps) {
                fp.x *= screenWidth;
                fp.y *= screenHeight;
                fp.frameDuration = FRAME_DURATION;
                task = new FrameAnimationTask(this, mBitmapsSmallFirework, 3, fp, true);
                postDelayedTask(task, fp.delay);
            }
        }
    }

    private void doBigFireworkAnimation() {
        CancelableTask task = new CancelableTask() {
            @Override
            public void doBusiness() {
                if (mBitmapsBigFirework == null || mBitmapsBigFirework.isEmpty()) {
                    return;
                }

                CustomAnimationDrawable drawable = new CustomAnimationDrawable(mDensity, mDensity,
                        0, false);
                drawable.setOnCompleteListener(new CustomAnimationDrawable.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                removeView(mIvBigFirework);
                            }
                        });
                    }
                });
                GiftUtils.addFrames(drawable, getResources(), mBitmapsBigFirework, FRAME_DURATION);
                drawable.setOneShot(true);
                mIvBigFirework.setVisibility(VISIBLE);
                mIvBigFirework.setImageDrawable(drawable);
                drawable.start();
            }
        };
        postDelayedTask(task, 6800L);
    }

    private void do1314Animation() {
        CancelableTask task = new CancelableTask() {
            @Override
            public void doBusiness() {
                String fileNameTemplate = "live_video_1314_%d.png";
                mBitmaps1314 = LiveResourceManager.getGiftBitmapList(getContext(),
                        28,
                        new String[] {
                                String.format(Locale.CHINA, fileNameTemplate, 1),
                                String.format(Locale.CHINA, fileNameTemplate, 2)
                        }, DisplayMetrics.DENSITY_XHIGH);// 原图按DENSITY_XHIGH制作
                mIv1314_1.setImageBitmap(mBitmaps1314.get(0));
                mIv1314_2.setImageBitmap(mBitmaps1314.get(1));

                final long duration = 500L;
                AlphaAnimation alpha1 = new AlphaAnimation(0f, 1f);
                alpha1.setDuration(duration);
                alpha1.setRepeatMode(Animation.REVERSE);
                alpha1.setRepeatCount(Animation.INFINITE);
                alpha1.setAnimationListener(new Animation.AnimationListener() {
                    boolean firstRepeat = true;
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        if (firstRepeat) {
                            firstRepeat = false;

                            AlphaAnimation alpha2 = new AlphaAnimation(0f, 1f);
                            alpha2.setDuration(duration);
                            alpha2.setRepeatMode(Animation.REVERSE);
                            alpha2.setRepeatCount(Animation.INFINITE);
                            mIv1314_2.setVisibility(VISIBLE);
                            mIv1314_2.startAnimation(alpha2);
                        }
                    }
                });
                mIv1314_1.setVisibility(VISIBLE);
                mIv1314_1.startAnimation(alpha1);
            }
        };
        postDelayedTask(task, 7600L);
    }

    private void doCastleLottie() {
        mIvCastleLottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                end();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mIvCastleLottie.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            boolean scheduled = false;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = animation.getAnimatedFraction();
                if (progress >= 0.85f && !scheduled) {
                    scheduled = true;

                    AlphaAnimation alpha = new AlphaAnimation(0.8f, 0f);
                    alpha.setDuration((long) (animation.getDuration() * (1f - progress)));
                    mIv1314_1.clearAnimation();
                    mIv1314_2.clearAnimation();
                    mIv1314_1.startAnimation(alpha);
                    mIv1314_2.startAnimation(alpha);
                }
            }
        });

        FileInputStream in;
        try {
            in = new FileInputStream(LiveResourceManager.GIFT_DIR + File.separator +
                    28 + File.separator + "castle_lottie_animation.json");
        } catch (IOException e) {
            e.printStackTrace();
            end();
            return;
        }
        mIvCastleLottie.setImageAssetDelegate(new ImageAssetDelegate() {
            @Override
            public Bitmap fetchBitmap(LottieImageAsset asset) {
                return LiveResourceManager.getGiftBitmap(getContext(), 28,
                        "castle_lottie_image" + File.separator + asset.getFileName(),
                        getResources().getDisplayMetrics().densityDpi);
            }
        });
        LottieComposition.Factory.fromInputStream(getContext(), in, new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                if (composition != null) {
                    mIvCastleLottie.setProgress(0);
                    mIvCastleLottie.setComposition(composition);
                    mIvCastleLottie.playAnimation();
                }
            }
        });
    }

    private void end() {
        mIv1314_1.setVisibility(GONE);
        mIv1314_2.setVisibility(GONE);
//                mIv1314_1.clearAnimation();
//                mIv1314_2.clearAnimation();
//        if (mBigGiftQueue != null && mGiftEffectParams != null) {
//            mBigGiftQueue.notifyUnitFinished(getUnitType(), mGiftEffectParams);
//        }
    }

    private void loadBitmaps() {
        Context context = getContext();
        int giftID = 28;
        int resourceDensity = DisplayMetrics.DENSITY_XHIGH;

        // 加载小烟花
        String[] fileNames = new String[SMALL_FIREWORK_FRAME_COUNT];
        String fileNameTemplate = "live_video_1314_small_firework_%d.png";
        for (int i = 1; i <= SMALL_FIREWORK_FRAME_COUNT; i++) {
            fileNames[i - 1] = String.format(Locale.CHINA, fileNameTemplate, i);
        }
        mBitmapsSmallFirework = LiveResourceManager.getGiftBitmapList(context, giftID, fileNames, resourceDensity);

        // 加载大烟花
        fileNames = new String[BIG_FIREWORK_FRAME_COUNT];
        fileNameTemplate = "live_video_1314_big_firework_%d.png";
        for (int i = 1; i <= BIG_FIREWORK_FRAME_COUNT; i++) {
            fileNames[i - 1] = String.format(Locale.CHINA, fileNameTemplate, i);
        }
        mBitmapsBigFirework = LiveResourceManager.getGiftBitmapList(context, giftID, fileNames, resourceDensity);
    }
}
