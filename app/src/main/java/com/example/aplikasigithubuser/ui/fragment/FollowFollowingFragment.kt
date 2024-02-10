package com.example.aplikasigithubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.databinding.FragmentFollowBinding
import com.example.aplikasigithubuser.ui.UserAdapter
import com.example.aplikasigithubuser.ui.UserViewModel

class FollowFollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME) ?: ""
            if (position == 1) {
                userViewModel.listFollowers.observe(viewLifecycleOwner, Observer { followersList ->
                    userAdapter.updateData(followersList)
                })
                userViewModel.fetchFollowers(username)
            } else {
                userViewModel.listFollowing.observe(viewLifecycleOwner, Observer { followingList ->
                    userAdapter.updateData(followingList)
                })
                userViewModel.fetchFollowing(username)
            }
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(emptyList())
        binding.rvFollow.adapter = userAdapter
        binding.rvFollow.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}