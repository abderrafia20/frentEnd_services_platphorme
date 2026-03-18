package com.example.clientcerviceplatphorm.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.clientcerviceplatphorm.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({

            val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

            if (isLoggedIn) {
                val userId = sharedPref.getString("userId", null)

                val intent = Intent(this, HomePageServises::class.java)
                intent.putExtra("id", userId)
                startActivity(intent)
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }

            finish()

        }, 2000) // 2 seconds splash
    }
}