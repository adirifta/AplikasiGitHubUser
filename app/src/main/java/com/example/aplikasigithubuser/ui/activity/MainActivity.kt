package com.example.aplikasigithubuser.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.databinding.ActivityMainBinding
import com.example.aplikasigithubuser.ui.UserAdapter
import com.example.aplikasigithubuser.ui.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel
    private var networkStatusReceiver: BroadcastReceiver? = null
    private var queryText: String? = null
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView(emptyList())
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        observeViewModel()
        setupSearchView()
        registerNetworkStatusReceiver()
        checkConnectionStatus()
        loadingIndicator = findViewById(R.id.loadingIndicator)
        loadingIndicator.visibility = View.VISIBLE

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_favorite -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("queryText", queryText)
        userViewModel.setNetworkAvailable(isNetworkAvailable())
    }

    override fun onResume() {
        super.onResume()
        checkConnectionStatus()
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
        userViewModel.userList.observe(this) { userList ->
            if (userList != null) {
                recyclerViewAdapter.updateData(userList)
                binding.loadingIndicator.visibility = View.GONE
            }
        }

        userViewModel.errorState.observe(this) { errorState ->
            if (errorState.first) {
                Toast.makeText(this, errorState.second, Toast.LENGTH_SHORT).show()
                binding.loadingIndicator.visibility = View.GONE
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    userViewModel.setSearchQuery(newText)
                    queryText=newText
                    performSearch(newText)
                } else {
                    userViewModel.setSearchQuery("adi")
                    queryText = ""
                    resetSearchView()
                }
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            userViewModel.setSearchQuery("adi")
            queryText = ""
            resetSearchView()
            true
        }

        userViewModel.searchQuery.value?.let { query ->
            binding.searchView.setQuery(query, false)
        }
    }

    private fun performSearch(query: String) {
        binding.loadingIndicator.visibility = View.VISIBLE
        userViewModel.searchUser(query)
    }

    private fun resetSearchView() {
        fetchData()
    }

    private fun fetchData() {
        val query = userViewModel.searchQuery.value ?: "adi"
        if (isNetworkAvailable()) {
            userViewModel.fetchData(query)
        }
    }

    private fun registerNetworkStatusReceiver() {
        networkStatusReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (isNetworkAvailable()) {
                    fetchData()
                    checkConnectionStatus()
                }
            }
        }
        registerReceiver(networkStatusReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun checkConnectionStatus() {
        if (isNetworkAvailable()) {
            Log.d("MainActivity", "200 OK - Connected to GitHub")
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unregisterNetworkStatusReceiver() {
        networkStatusReceiver?.let { unregisterReceiver(it) }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}