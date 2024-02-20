package com.example.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> get() = _listFollowing

    private val _errorState = MutableLiveData<Pair<Boolean, String>>()

    private fun handleFailure(message: String) {
        _errorState.value = Pair(true, message)
    }

    fun fetchFollowing(username: String) {
        val apiService = ApiConfig.getApiService()
        val followingCall = apiService.getUserFollowing(username)
        followingCall.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful) {
                    _listFollowing.value = response.body() ?: emptyList()
                } else {
                    handleFailure("Failed to fetch following data. Please try again later.")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                handleFailure("Network error occurred. Please check your internet connection.")
            }
        })
    }
}