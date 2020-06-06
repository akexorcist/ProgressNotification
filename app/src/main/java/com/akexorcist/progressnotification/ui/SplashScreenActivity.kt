package com.akexorcist.progressnotification.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.progressnotification.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setupView()
    }

    private fun setupView() {
        btnNext?.setOnClickListener { goToMainScreen() }
    }

    private fun goToMainScreen() {
        startActivity(Intent(this, ApplyAccountActivity::class.java))
    }
}
