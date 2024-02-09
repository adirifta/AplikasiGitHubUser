package com.example.aplikasigithubuser.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.databinding.ActivityMainBinding
import com.example.aplikasigithubuser.ui.UserAdapter
import com.example.aplikasigithubuser.ui.UserViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel
    private var networkStatusReceiver: BroadcastReceiver? = null
    private var queryText: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val bookmarkedList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView(emptyList())

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        observeViewModel()

        setupSearchView()

        fetchData()

        registerNetworkStatusReceiver()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("queryText", queryText)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkStatusReceiver()
    }

    private fun setupRecyclerView(userList: List<ItemsItem>) {
        recyclerViewAdapter = UserAdapter(userList)
        binding.recyclerView.apply {
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observeViewModel() {
        userViewModel.userList.observe(this, Observer { userList ->
            recyclerViewAdapter.updateData(userList)
        })

        userViewModel.isLoading.observe(this, Observer { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    resetSearchView()
                } else {
                    newText?.let {
                        performSearch(it)
                    }
                }
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        binding.loadingIndicator.visibility = View.VISIBLE
        userViewModel.searchUser(query)
    }

    private fun resetSearchView() {
        binding.loadingIndicator.visibility = View.GONE
        fetchData()
    }

    private fun fetchData() {
        userViewModel.fetchData("adi")
    }

    private fun registerNetworkStatusReceiver() {
        networkStatusReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (isNetworkAvailable()) {
                    fetchData()
                }
            }
        }
        registerReceiver(networkStatusReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun unregisterNetworkStatusReceiver() {
        networkStatusReceiver?.let { unregisterReceiver(it) }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}