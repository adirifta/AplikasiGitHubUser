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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private fun handleFailure(message: String) {
        _errorState.value = Pair(true, message)
    }

    fun fetchFollowers(username: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val followersCall = apiService.getUserFollowers(username)
        followersCall.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body() ?: emptyList()
                } else {
                    handleFailure("Failed to fetch followers data. Please try again later.")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                handleFailure("Network error occurred. Please check your internet connection.")
            }
        })
    }
}