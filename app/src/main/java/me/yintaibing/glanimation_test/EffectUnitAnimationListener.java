package me.yintaibing.glanimation_test;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import java.lang.ref.WeakReference;

/**
 * 礼物特效部件动画监听器。主要用于部件动画结束时设GONE。
 *
 * @author yintaibing
 * @date 2019/02/16
 */
public class EffectUnitAnimationListener implements Animation.AnimationListener {
    private WeakReference<View> mViewRef;

//    private BigGiftQueue mBigGiftQueue;
//    private int mUnitType;
//    private GiftEffectParams mParams;

    private boolean mOnEndCalled;// onEnd是否已被执行
    private Runnable mOnEndTask = new Runnable() {
        @Override
        public void run() {
            onEnd();
        }
    };

    // goneOnEnd
    private EffectUnitAnimationListener(View view, long postDelay) {
        mViewRef = new WeakReference<>(view);

        view.postDelayed(mOnEndTask, postDelay);
    }

//    // finishOnEnd
//    private EffectUnitAnimationListener(View view,
//                                        long postDelay,
//                                        BigGiftQueue bigGiftQueue,
//                                        int unitType,
//                                        GiftEffectParams params) {
//        mViewRef = new WeakReference<>(view);
//        mBigGiftQueue = bigGiftQueue;
//        mUnitType = unitType;
//        mParams = params;
//
//        view.postDelayed(mOnEndTask, postDelay);
//    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        onEnd();
    }

    private synchronized void onEnd() {
        if (!mOnEndCalled) {
            mOnEndCalled = true;
            View view = mViewRef.get();
            if (view != null) {
                view.setVisibility(View.GONE);
                view.removeCallbacks(mOnEndTask);
            }
//            if (mBigGiftQueue != null && mParams != null) {
//                // 仅通过finishOnEnd创建监听器时，mBigGiftQueue才不为空
//                mBigGiftQueue.notifyUnitFinished(mUnitType, mParams);
//            }
        }

        mOnEndTask = null;
//        mBigGiftQueue = null;
//        mParams = null;
    }

    /**
     * 设置动画监听器，使得当View的动画结束时使它GONE
     *
     * @param view      View对象
     * @param animation 动画对象
     */
    public static void goneOnEnd(View view, Animation animation) {
        if (view == null) {
            return;
        }
        long postDelay = computePostDelay(animation);
        if (postDelay <= 0) {
            return;
        }
        animation.setAnimationListener(new EffectUnitAnimationListener(view, postDelay));
    }

//    /**
//     * 设置动画监听器，使得当View的动画结束时使整个EffectUnit结束
//     *
//     * @param view         View对象
//     * @param animation    动画对象
//     * @param bigGiftQueue 大礼物对象
//     * @param unitType     EffectUnit的类型
//     * @param params       礼物动画数据对象
//     */
//    public static void finishOnEnd(View view,
//                                   Animation animation,
//                                   BigGiftQueue bigGiftQueue,
//                                   int unitType,
//                                   GiftEffectParams params) {
//        if (view == null) {
//            return;
//        }
//        long postDelay = computePostDelay(animation);
//        if (postDelay <= 0) {
//            return;
//        }
//        animation.setAnimationListener(new EffectUnitAnimationListener(view, postDelay,
//                bigGiftQueue, unitType, params));
//    }

    /**
     * @param animation 动画对象
     * @return 计算mOnEndTask的postDelay
     */
    private static long computePostDelay(Animation animation) {
        if (animation == null) {
            return 0L;
        }
        if (animation instanceof AnimationSet) {
            // AnimationSet的duration是正确的，但还需考虑到startOffset
            long delay = 0L;
            for (Animation a : ((AnimationSet) animation).getAnimations()) {
                delay = Math.max(delay, a.getStartOffset() + a.getDuration());
            }
            return delay;
        }
        return animation.getStartOffset() + animation.getDuration();
    }
}
