package me.yintaibing.glanimation_test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

public class TestBitmapLoadActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bitmap_load);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                load();
            }
        }, 8000L);
    }

    Bitmap bitmap;
    private void load() {
        Bitmap b = LiveResourceManager.getGiftBitmap(this, 3,
                "bg_live_video_romantic_date_flower.png", DisplayMetrics.DENSITY_XHIGH);
        Log.e("TestBitmapLoadActivity", "w=" + b.getWidth()
                + " h=" + b.getHeight()
                + " byteCount=" + b.getByteCount()
                + " allocByteCount=" + (Build.VERSION.SDK_INT >= 19 ? b.getAllocationByteCount() : 0));
        bitmap = b;
    }
}
