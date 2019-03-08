package me.yintaibing.glanimation_test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class NativeCastleActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_castle);

        final Castle1314Widget castle1314Widget = findViewById(R.id.castle);
        castle1314Widget.postDelayed(new Runnable() {
            @Override
            public void run() {
                castle1314Widget.showEffect();
            }
        }, 1000L);
    }
}
