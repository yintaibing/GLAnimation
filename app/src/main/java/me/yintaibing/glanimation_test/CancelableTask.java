package me.yintaibing.glanimation_test;

/**
 * 可取消任务
 *
 * @author yintaibing 2017/10/25
 */
public abstract class CancelableTask implements Runnable {
    private boolean mIsCanceled = false;

    @Override
    public void run() {
        if (!mIsCanceled) {
            doBusiness();
        }
    }

    public void cancel() {
        mIsCanceled = true;
    }

    public boolean isCanceled() {
        return mIsCanceled;
    }

    public abstract void doBusiness();
}
