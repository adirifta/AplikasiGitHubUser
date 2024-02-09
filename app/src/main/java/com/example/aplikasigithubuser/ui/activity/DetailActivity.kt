package com.example.aplikasigithubuser.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.data.response.DetailUserResponse
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.data.retrofit.ApiConfig
import com.example.aplikasigithubuser.databinding.ActivityDetailBinding
import com.example.aplikasigithubuser.ui.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("USERNAME")

        if (username != null) {
            setupViewPager(username)
            fetchUserDetails(username)
            fetchFollowersCount(username)
            fetchFollowingCount(username)
        }
        val backButton: ImageView = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fetchUserDetails(username: String) {
        binding.loadingIndicator.visibility = View.VISIBLE

        val apiService = ApiConfig.getApiService()

        val userDetailsCall = apiService.getUserDetails(username)
        userDetailsCall.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                binding.loadingIndicator.visibility = View.GONE

                if (response.isSuccessful) {
                    val userDetail = response.body()
                    userDetail?.let {
                        displayUserDetails(it)
                    }

                    fetchFollowersCount(username)
                    fetchFollowingCount(username)
                } else {
                    Log.e("DetailActivity", "API call not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                binding.loadingIndicator.visibility = View.GONE

                Log.e("DetailActivity", "API call failed", t)
            }
        })
    }

    private fun displayUserDetails(userDetail: DetailUserResponse) {
        binding.detailUsername.text = userDetail.login
        binding.detailGithubName.text = userDetail.name ?: ""

        Picasso.get().load(userDetail.avatarUrl).into(binding.detailAvatar)

        binding.followersCount.text = "${userDetail.followers}"
        binding.followingCount.text = "${userDetail.following}"
    }

    private fun setupViewPager(username: String) {
        sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username

        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> ""
            }
        }.attach()
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
                        updateFollowersCount(followersCount)
                    }
                } else {
                    Log.e("DetailActivity", "Followers API call not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e("DetailActivity", "Followers API call failed", t)
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
                        updateFollowingCount(followingCount)
                    }
                } else {
                    Log.e("DetailActivity", "Following API call not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e("DetailActivity", "Following API call failed", t)
            }
        })
    }

    private fun updateFollowersCount(count: Int) {
        binding.followersCount.text = "$count"
    }

    private fun updateFollowingCount(count: Int) {
        binding.followingCount.text = "$count"
    }
}