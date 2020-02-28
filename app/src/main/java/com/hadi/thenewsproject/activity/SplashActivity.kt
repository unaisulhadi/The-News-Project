package com.hadi.thenewsproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.hadi.thenewsproject.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val timer = object : CountDownTimer(2900, 1000) {

            override fun onFinish() {

                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                finish()
            }

            override fun onTick(p0: Long) {

            }
        }

        timer.start()

    }
}
