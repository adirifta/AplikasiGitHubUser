package com.example.aplikasigithubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.data.response.ItemsItem
import com.example.aplikasigithubuser.ui.activity.DetailActivity
import com.squareup.picasso.Picasso

class UserAdapter(private var itemList: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarImageView: ImageView = itemView.findViewById(R.id.itemAvatar)
        private val usernameTextView: TextView = itemView.findViewById(R.id.itemName)
        private val typeTextView: TextView = itemView.findViewById(R.id.itemType)

        private lateinit var item: ItemsItem

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("USERNAME", item.login)
                context.startActivity(intent)
            }
        }

        fun bind(item: ItemsItem) {
            this.item = item

            Picasso.get().load(item.avatarUrl).into(avatarImageView)

            usernameTextView.text = item.login
            typeTextView.text = item.type
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItemList: List<ItemsItem>) {
        itemList = newItemList
        notifyDataSetChanged()
    }
}