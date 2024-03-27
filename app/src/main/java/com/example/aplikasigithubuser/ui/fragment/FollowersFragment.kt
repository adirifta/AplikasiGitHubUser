package com.example.aplikasigithubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.databinding.FragmentFollowBinding
import com.example.aplikasigithubuser.ui.adapter.UserAdapter
import com.example.aplikasigithubuser.ui.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        followersViewModel = ViewModelProvider(this)[FollowersViewModel::class.java]

        arguments?.let {
            val username = it.getString(ARG_USERNAME) ?: ""
            followersViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                isLoading?.let { isLoadingValue ->
                    if (isLoadingValue) {
                        binding.loadingIndicator.visibility = View.VISIBLE
                    } else {
                        binding.loadingIndicator.visibility = View.GONE
                    }
                }
            }
            followersViewModel.listFollowers.observe(viewLifecycleOwner) { followersList ->
                userAdapter.updateData(followersList)
            }
            followersViewModel.fetchFollowers(username)
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(emptyList())
        binding.rvFollow.adapter = userAdapter
        binding.rvFollow.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }
}