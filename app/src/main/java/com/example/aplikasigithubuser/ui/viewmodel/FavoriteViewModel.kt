package com.example.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasigithubuser.data.FavoriteUserRepository
import com.example.aplikasigithubuser.data.database.FavoriteUser
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteUserRepository) : ViewModel() {

    val allFavoriteUsers: LiveData<List<FavoriteUser>> = repository.getAllNotes()

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            repository.delete(favoriteUser)
        }
    }
}


