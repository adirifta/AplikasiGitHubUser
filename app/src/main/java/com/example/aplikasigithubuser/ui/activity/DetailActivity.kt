package com.example.aplikasigithubuser.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.data.response.DetailUserResponse
import com.example.aplikasigithubuser.databinding.ActivityDetailBinding
import com.example.aplikasigithubuser.ui.SectionsPagerAdapter
import com.example.aplikasigithubuser.ui.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("USERNAME")

        if (username != null) {
            setupViewPager(username)
            viewModel.fetchUserDetails(username)
        }

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        viewModel.userDetails.observe(this) { userDetails ->
            userDetails?.let {
                displayUserDetails(it)
            }
        }

        viewModel.followersCount.observe(this) { count ->
            count?.let {
                updateFollowersCount(it)
            }
        }

        viewModel.followingCount.observe(this) { count ->
            count?.let {
                updateFollowingCount(it)
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Log.e("DetailActivity", errorMessage)
            }
        }
    }

    private fun displayUserDetails(userDetail: DetailUserResponse) {
        binding.detailUsername.text = userDetail.login
        binding.detailGithubName.text = userDetail.name ?: ""
        Picasso.get().load(userDetail.avatarUrl).into(binding.detailAvatar)
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

    private fun updateFollowersCount(count: Int) {
        binding.followersCount.text = "$count"
    }

    private fun updateFollowingCount(count: Int) {
        binding.followingCount.text = "$count"
    }
}