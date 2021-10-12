package com.abdulqohar.hagglex.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdulqohar.hagglex.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}