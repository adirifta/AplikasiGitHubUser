package com.example.aplikasigithubuser.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aplikasigithubuser.data.database.FavoriteRoomDatabase
import com.example.aplikasigithubuser.data.database.FavoriteUser
import com.example.aplikasigithubuser.data.database.FavoriteUserDao
import com.example.aplikasigithubuser.data.response.DetailUserResponse
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _userDetails = MutableLiveData<DetailUserResponse>()
    val userDetails: LiveData<DetailUserResponse> = _userDetails

    private val _followersCount = MutableLiveData<Int>()
    val followersCount: LiveData<Int> = _followersCount

    private val _followingCount = MutableLiveData<Int>()
    val followingCount: LiveData<Int> = _followingCount

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()

    private var userDao: FavoriteUserDao? = null
    private var userDb: FavoriteRoomDatabase? = null

    init {
        userDb = FavoriteRoomDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

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
                    Toast.makeText(getApplication(), "API call not successful. Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = "API call failed: ${t.message}"
                Toast.makeText(getApplication(), "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(getApplication(), "Followers API call not successful. Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(ignoredCall: Call<List<ItemsItem>>, t: Throwable) {
                _error.value = "Followers API call failed: ${t.message}"
                Toast.makeText(getApplication(), "Followers API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(getApplication(), "Following API call not successful. Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(ignoredCall: Call<List<ItemsItem>>, t: Throwable) {
                _error.value = "Following API call failed: ${t.message}"
                Toast.makeText(getApplication(), "Following API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(username, id, avatarUrl)
            userDao?.addToFavorite(user)
        }
    }

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.deleteUser(id)
        }
    }
}