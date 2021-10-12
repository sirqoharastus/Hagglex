package com.abdulqohar.hagglex.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import com.abdulqohar.hagglex.R
import com.abdulqohar.hagglex.databinding.ActivityDashbordBinding
import com.google.android.material.snackbar.Snackbar

class DashbordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityDashbordBinding = ActivityDashbordBinding.inflate(layoutInflater)
            setContentView(binding.root)

        // Implementation for logout button
        binding.dashbordActivityToolbar.apply {
            inflateMenu(R.menu.menu)
            setOnMenuItemClickListener {
                when (it.itemId){
                    R.id.logout ->{
                        startActivity(Intent(this@DashbordActivity, AuthActivity::class.java))
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }
        }
    }
    //implementation for double tap back button to exit app
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Snackbar.make(findViewById(R.id.dashboard_layout), "Please click BACK again to exit", Snackbar.LENGTH_LONG).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
    //Inflating options menu with menu resource file
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    //Handling menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout -> {
                startActivity(Intent(this, AuthActivity::class.java))
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}