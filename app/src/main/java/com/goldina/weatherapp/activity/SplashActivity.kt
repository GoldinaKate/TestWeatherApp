package com.goldina.weatherapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.goldina.weatherapp.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val home = Intent(applicationContext, MainActivity::class.java)
            startActivity(home)
            finish()
        }, SPLASH_TIME_OUT)
    }

    companion object {
        private const val SPLASH_TIME_OUT = 1000L
    }
}