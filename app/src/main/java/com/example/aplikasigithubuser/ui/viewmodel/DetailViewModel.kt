package com.example.aplikasigithubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikasigithubuser.data.FavoriteUserRepository
import com.example.aplikasigithubuser.data.database.FavoriteUser
import com.example.aplikasigithubuser.data.response.DetailUserResponse
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    constructor() : this(Application())

    private val _userDetails = MutableLiveData<DetailUserResponse>()
    val userDetails: LiveData<DetailUserResponse> = _userDetails

    private val _followersCount = MutableLiveData<Int>()
    val followersCount: LiveData<Int> = _followersCount

    private val _followingCount = MutableLiveData<Int>()
    val followingCount: LiveData<Int> = _followingCount


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()

    fun fetchUserDetails(username: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()

        val userDetailsCall = apiService.getUserDetails(username)
        userDetailsCall.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    val userDetail = response.body()
                    userDetail?.let {
                        _userDetails.value = it
                    }
                    fetchFollowersCount(username)
                    fetchFollowingCount(username)
                } else {
                    _error.value = "API call not successful. Code: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = "API call failed: ${t.message}"
            }
        })
    }

    private fun fetchFollowersCount(username: String) {
        val apiService = ApiConfig.getApiService()
        val followersCall = apiService.getUserFollowers(username)
        followersCall.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(ignoredCall: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful) {
                    val followersList = response.body()
                    followersList?.let {
                        val followersCount = it.size
                        _followersCount.value = followersCount
                    }
                } else {
                    _error.value = "Followers API call not successful. Code: ${response.code()}"
                }
            }

            override fun onFailure(ignoredCall: Call<List<ItemsItem>>, t: Throwable) {
                _error.value = "Followers API call failed: ${t.message}"
            }
        })
    }

    private fun fetchFollowingCount(username: String) {
        val apiService = ApiConfig.getApiService()
        val followingCall = apiService.getUserFollowing(username)
        followingCall.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(ignoredCall: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful) {
                    val followingList = response.body()
                    followingList?.let {
                        val followingCount = it.size
                        _followingCount.value = followingCount
                    }
                } else {
                    _error.value = "Following API call not successful. Code: ${response.code()}"
                }
            }

            override fun onFailure(ignoredCall: Call<List<ItemsItem>>, t: Throwable) {
                _error.value = "Following API call failed: ${t.message}"
            }
        })
    }

    private val repository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            mFavoriteUserRepository.insert(favoriteUser)
        }
    }

    fun updateFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            mFavoriteUserRepository.update(favoriteUser)
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            mFavoriteUserRepository.delete(favoriteUser)
        }
    }

    fun toggleFavoriteUser(username: String, isFavorite: Boolean) {
        val favoriteUser = FavoriteUser(login = username, avatarUrl = "", isFavorite = isFavorite)
        if (isFavorite) {
            insertFavoriteUser(favoriteUser)
        } else {
            deleteFavoriteUser(favoriteUser)
        }
    }
}