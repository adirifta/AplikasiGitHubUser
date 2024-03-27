package com.example.aplikasigithubuser.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.databinding.ItemLayoutBinding
import com.example.aplikasigithubuser.ui.activity.DetailActivity
import com.squareup.picasso.Picasso

class UserAdapter(private var itemList: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size
    inner class ViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemsItem) {
            Picasso.get().load(item.avatarUrl).into(binding.itemAvatar)
            binding.itemName.text = item.login
            binding.itemType.text = item.type

            binding.root.setOnClickListener {
                val context: Context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("USERNAME", item.login)
                    putExtra(DetailActivity.EXTRA_ID, item.id)
                    putExtra(DetailActivity.EXTRA_URL, item.avatarUrl)
                }
                context.startActivity(intent)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItemList: List<ItemsItem>) {
        itemList = newItemList
        notifyDataSetChanged()
    }
}