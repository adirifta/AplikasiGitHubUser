package com.example.aplikasigithubuser.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.data.response.DetailUserResponse
import com.example.aplikasigithubuser.databinding.ActivityDetailBinding
import com.example.aplikasigithubuser.ui.adapter.SectionsPagerAdapter
import com.example.aplikasigithubuser.ui.viewmodel.DetailViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private val viewModel: DetailViewModel by viewModels()
    private var _isChecked = false
    private lateinit var sharedPreferences: SharedPreferences


    companion object{
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val FAVORITE_PREFS = "favorite_prefs"
        const val FAVORITE_STATUS = "favorite_status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(FAVORITE_PREFS, Context.MODE_PRIVATE)

        val username = intent.getStringExtra("USERNAME")
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        if (username != null) {
            setupViewPager(username)
            viewModel.fetchUserDetails(username)
            binding.loadingIndicator.visibility = View.VISIBLE
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

        _isChecked = sharedPreferences.getBoolean(FAVORITE_STATUS + id, false)
        val favoriteButton: FloatingActionButton = findViewById(R.id.favoriteButton)
        favoriteButton.setImageResource(if (_isChecked) R.drawable.ic_favorite else R.drawable.ic_favorite_border)

        favoriteButton.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                if (avatarUrl != null && username != null) {
                    viewModel.addToFavorite(username, id, avatarUrl)
                }
                favoriteButton.setImageResource(R.drawable.ic_favorite)
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.removeFromFavorite(id)
                favoriteButton.setImageResource(R.drawable.ic_favorite_border)
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
            sharedPreferences.edit().putBoolean(FAVORITE_STATUS + id, _isChecked).apply()
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
        binding.loadingIndicator.visibility = View.GONE
    }

    private fun updateFollowingCount(count: Int) {
        binding.followingCount.text = "$count"
    }
}