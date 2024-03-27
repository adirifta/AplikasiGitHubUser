package com.example.aplikasigithubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aplikasigithubuser.data.database.FavoriteRoomDatabase
import com.example.aplikasigithubuser.data.database.FavoriteUser
import com.example.aplikasigithubuser.data.database.FavoriteUserDao

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val _userList = MutableLiveData<List<FavoriteUser>>()
    val userList: LiveData<List<FavoriteUser>> get() = _userList

    private var userDao: FavoriteUserDao?
    private var userDb: FavoriteRoomDatabase?

    init {
        userDb = FavoriteRoomDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavoriteUser()
    }
}