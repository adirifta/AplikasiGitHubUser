package com.example.aplikasigithubuser.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.SettingPreferences
import com.example.aplikasigithubuser.ViewModelFactory
import com.example.aplikasigithubuser.dataStore
import com.example.aplikasigithubuser.ui.viewmodel.SplashViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val viewModel = ViewModelProvider(this,
            ViewModelFactory(pref))[SplashViewModel::class.java]

        viewModel.getThemSettings().observe(this){isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)

        supportActionBar?.hide()
    }
}