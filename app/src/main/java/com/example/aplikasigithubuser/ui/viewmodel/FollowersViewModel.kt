package com.example.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> get() = _listFollowers

    private val _errorState = MutableLiveData<Pair<Boolean, String>>()

    private fun handleFailure(message: String) {
        _errorState.value = Pair(true, message)
    }

    fun fetchFollowers(username: String) {
        val apiService = ApiConfig.getApiService()
        val followersCall = apiService.getUserFollowers(username)
        followersCall.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful) {
                    _listFollowers.value = response.body() ?: emptyList()
                } else {
                    handleFailure("Failed to fetch followers data. Please try again later.")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                handleFailure("Network error occurred. Please check your internet connection.")
            }
        })
    }
}