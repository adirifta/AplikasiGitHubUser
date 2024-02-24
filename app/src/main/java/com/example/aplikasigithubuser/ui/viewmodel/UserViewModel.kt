package com.example.aplikasigithubuser.ui.viewmodel

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
    private var isNetworkAvailable: Boolean = false
    private val _isLoading = MutableLiveData<Boolean>()
    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> get() = _userList

    private val _errorState = MutableLiveData<Pair<Boolean, String>>()
    val errorState: LiveData<Pair<Boolean, String>> get() = _errorState

    private fun handleFailure(message: String) {
        _errorState.value = Pair(true, message)
    }

    fun setNetworkAvailable(available: Boolean) {
        isNetworkAvailable = available
    }

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
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