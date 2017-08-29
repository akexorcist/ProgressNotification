package com.akexorcist.progressnotification.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.akexorcist.progressnotification.R;

public class SplashScreenActivity extends AppCompatActivity {
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        bindView();
        setupView();
    }

    private void bindView() {
        btnNext = findViewById(R.id.btn_next);
    }

    private void setupView() {
        btnNext.setOnClickListener(view -> goToMainScreen());
    }

    private void goToMainScreen() {
        startActivity(new Intent(SplashScreenActivity.this, ApplyAccountActivity.class));
    }
}
