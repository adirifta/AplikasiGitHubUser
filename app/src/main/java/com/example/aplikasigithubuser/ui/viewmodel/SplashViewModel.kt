package com.example.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.aplikasigithubuser.SettingPreferences

class SplashViewModel (private val pref: SettingPreferences) : ViewModel() {
    fun getThemSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}