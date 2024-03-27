package com.example.aplikasigithubuser.ui.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.data.database.FavoriteUser
import com.example.aplikasigithubuser.databinding.ActivityFavoriteBinding
import com.example.aplikasigithubuser.ui.adapter.FavoriteAdapter
import com.example.aplikasigithubuser.ui.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        setupRecyclerView()
        observeViewModel()

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        viewModel.getFavoriteUser()?.observe(this) { favoriteUsers ->
            val favoriteItemList = favoriteUsers.map {
                FavoriteUser(it.login, it.id, it.avatarUrl)
            }
            favoriteAdapter = FavoriteAdapter(this, favoriteItemList)
            binding.recyclerViewFavorite.adapter = favoriteAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.userList.observe(this) { userList ->
            if (userList != null) {
                favoriteAdapter.updateDataFavorite(userList)
            }
        }
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(this, emptyList())
        binding.recyclerViewFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }
    }
}