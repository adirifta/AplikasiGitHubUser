package com.example.aplikasigithubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.databinding.FragmentFollowBinding
import com.example.aplikasigithubuser.ui.UserAdapter
import com.example.aplikasigithubuser.ui.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: FollowingViewModel
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

        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]

        arguments?.let {
            val username = it.getString(ARG_USERNAME) ?: ""
            followingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                isLoading?.let { isLoadingValue ->
                    if (isLoadingValue) {
                        binding.loadingIndicator.visibility = View.VISIBLE
                    } else {
                        binding.loadingIndicator.visibility = View.GONE
                    }
                }
            }
            followingViewModel.listFollowing.observe(viewLifecycleOwner) { followingList ->
                userAdapter.updateData(followingList)
            }
            followingViewModel.fetchFollowing(username)
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(emptyList())
        binding.rvFollow.adapter = userAdapter
        binding.rvFollow.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowingFragment {
            val fragment = FollowingFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }
}