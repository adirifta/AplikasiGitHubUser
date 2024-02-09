package com.example.aplikasigithubuser.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.databinding.ActivityBookmarkBinding

class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("bookmark_pref", Context.MODE_PRIVATE)
        val bookmarkedUsernames = sharedPreferences.all.keys.toList()

        // Tampilkan daftar item yang ditandai sebagai bookmark
        // Anda bisa menggunakan RecyclerView atau tampilan lain sesuai kebutuhan
    }
}