package com.example.aplikasigithubuser.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.aplikasigithubuser.data.database.FavoriteRoomDatabase
import com.example.aplikasigithubuser.data.database.FavoriteUser
import com.example.aplikasigithubuser.data.database.FavoriteUserDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(private val context: Context) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(context)
        mFavoriteUserDao = db.favoriteUserDao()
    }
    fun getAllNotes(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllNotes()
    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }
    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }
    fun update(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.update(favoriteUser) }
    }
}