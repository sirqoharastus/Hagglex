package com.abdulqohar.hagglex.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.abdulqohar.hagglex.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //Implementation to delay splash screen for 3 seconds the launch auth activity
        Handler(Looper.getMainLooper())
            .postDelayed(
                {startActivity(Intent(this, AuthActivity::class.java))},
                3000
            )
        //Implementation to kill this activity once auth activity is lauched
        Handler(Looper.getMainLooper()).postDelayed({this.finish()}, 4000)
    }
}