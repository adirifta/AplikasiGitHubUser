package com.example.aplikasigithubuser.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aplikasigithubuser.ui.fragment.FollowersFragment
import com.example.aplikasigithubuser.ui.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FollowersFragment.newInstance(username)
            }
            1 -> {
                FollowingFragment.newInstance(username)
            }
            else -> {
                Fragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}