package com.example.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.data.response.DetailUserResponse
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _userDetails = MutableLiveData<DetailUserResponse>()
    val userDetails: LiveData<DetailUserResponse> = _userDetails

    private val _followersCount = MutableLiveData<Int>()
    val followersCount: LiveData<Int> = _followersCount

    private val _followingCount = MutableLiveData<Int>()
    val followingCount: LiveData<Int> = _followingCount


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchUserDetails(username: String) {

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
                _error.value = "API call failed: ${t.message}"
            }
        })
    }

    private fun fetchFollowersCount(username: String) {
        val apiService = ApiConfig.getApiService()
        val followersCall = apiService.getUserFollowers(username)
        followersCall.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
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

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _error.value = "Followers API call failed: ${t.message}"
            }
        })
    }

    private fun fetchFollowingCount(username: String) {
        val apiService = ApiConfig.getApiService()
        val followingCall = apiService.getUserFollowing(username)
        followingCall.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
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

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _error.value = "Following API call failed: ${t.message}"
            }
        })
    }
}