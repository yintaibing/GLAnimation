package me.yintaibing.glanimation_test;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 添加帧动画任务
 *
 * @author yintaibing
 * @date 2017/12/11
 */

public class FrameAnimationTask extends CancelableTask {
    private WeakReference<ViewGroup> mParent;
    private List<Bitmap> mBitmaps;
    private int mChildPos;
    private FrameParams mParams;
    private boolean mNeedRemoveOnEnd;

    public FrameAnimationTask(ViewGroup parent, List<Bitmap> bitmaps, int childPos,
                              FrameParams params, boolean needRemoveOnEnd) {
        mParent = new WeakReference<>(parent);
        mBitmaps = bitmaps;
        mChildPos = childPos;
        mParams = params;
        mNeedRemoveOnEnd = needRemoveOnEnd;
    }

    @Override
    public void doBusiness() {
        if (checkValid()) {
            ViewGroup parent = mParent.get();
            Resources resources = parent.getResources();
            final ImageView imageView = new ImageView(parent.getContext());
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
//                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT));
            // 上面这个写法居然margin不起作用
            ViewGroup.LayoutParams lp;
            final int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
            if (parent instanceof FrameLayout) {
                lp = new FrameLayout.LayoutParams(wrapContent, wrapContent);
            } else if (parent instanceof RelativeLayout) {
                lp = new RelativeLayout.LayoutParams(wrapContent, wrapContent);
            } else if (parent instanceof LinearLayout) {
                lp = new LinearLayout.LayoutParams(wrapContent, wrapContent);
            } else {
                lp = new ViewGroup.LayoutParams(wrapContent, wrapContent);
            }
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) lp).leftMargin = (int) mParams.x;
                ((ViewGroup.MarginLayoutParams) lp).topMargin = (int) mParams.y;
            }
            parent.addView(imageView, Math.min(mChildPos, parent.getChildCount()), lp);

            CustomAnimationDrawable drawable = GiftUtils.createCustomAnimationDrawable(
                    resources, mBitmaps, true, mParams);
            if (mNeedRemoveOnEnd) {
                drawable.setOnCompleteListener(new CustomAnimationDrawable.OnCompleteListener() {
                    @Override
                    void onComplete() {
                        ViewGroup parent = getParent();
                        if (parent != null) {
                            parent.post(new Runnable() {
                                @Override
                                public void run() {
                                    ViewGroup parent = getParent();
                                    if (parent != null) {
                                        // 蜜汁缩进。。。
                                        parent.removeView(imageView);
                                    }
                                }
                            });
                        }
                    }
                });
            }

            imageView.setImageDrawable(drawable);
            drawable.start();
        }
    }

    private boolean checkValid() {
        return mParent != null && mParent.get() != null && mBitmaps != null && !mBitmaps.isEmpty()
                && mChildPos >= 0;
    }

    private ViewGroup getParent() {
        return mParent != null ? mParent.get() : null;
    }
}
