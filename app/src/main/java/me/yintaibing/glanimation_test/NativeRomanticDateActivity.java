package me.yintaibing.glanimation_test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class NativeRomanticDateActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_romantic_date);

        final RomanticDateWidget romanticDateWidget = findViewById(R.id.romantic_date);
        romanticDateWidget.postDelayed(new Runnable() {
            @Override
            public void run() {
                romanticDateWidget.showEffect();
            }
        }, 1000L);
    }
}
