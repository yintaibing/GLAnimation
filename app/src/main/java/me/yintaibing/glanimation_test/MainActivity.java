package me.yintaibing.glanimation_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import me.yintaibing.ppt.PPTActivity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test_gl_painter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, TestGLPainterActivity.class));
                startActivity(new Intent(MainActivity.this, PPTActivity.class));
            }
        });
        findViewById(R.id.btn_test_bitmap_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestBitmapLoadActivity.class));
            }
        });
        findViewById(R.id.btn_gl_romantic_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GLRomanticDateActivity.class));
            }
        });
        findViewById(R.id.btn_native_romantic_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NativeRomanticDateActivity.class));
            }
        });
        findViewById(R.id.btn_gl_castle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GLCastleActivity.class));
            }
        });
        findViewById(R.id.btn_native_castle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, NativeCastleActivity.class));
                startActivity(new Intent(MainActivity.this, CombinePngActivity.class));
            }
        });
    }
}
