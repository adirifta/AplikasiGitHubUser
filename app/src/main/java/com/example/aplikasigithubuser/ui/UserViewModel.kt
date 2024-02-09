package com.example.aplikasigithubuser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.data.response.GithubResponse
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> get() = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> get() = _listFollowers

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> get() = _listFollowing

    private val _errorState = MutableLiveData<Pair<Boolean, String>>()
    val errorState: LiveData<Pair<Boolean, String>> get() = _errorState

    private fun handleFailure(message: String) {
        _errorState.value = Pair(true, message)
    }

    fun fetchData(username: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val userCall = apiService.searchUsers(username)
        userCall.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val items = response.body()?.items
                    _userList.value = items ?: emptyList()
                } else {
                    handleFailure("Failed to fetch user data. Please try again later.")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                handleFailure("Network error occurred. Please check your internet connection.")
            }
        })
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

    fun searchUser(query: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val userCall = apiService.searchUsers(query)
        userCall.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val items = response.body()?.items
                    _userList.value = items ?: emptyList()
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                handleFailure("Network error occurred. Please check your internet connection.")
            }
        })
    }
}