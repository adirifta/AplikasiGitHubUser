package com.example.aplikasigithubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aplikasigithubuser.ui.fragment.FollowFollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFollowingFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFollowingFragment.ARG_POSITION, position + 1)
            putString(FollowFollowingFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}