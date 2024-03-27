    package com.example.aplikasigithubuser.ui

    import android.content.Context
    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.RecyclerView
    import com.example.aplikasigithubuser.data.database.FavoriteUser
    import com.example.aplikasigithubuser.databinding.ItemFavoriteBinding
    import com.squareup.picasso.Picasso

    class FavoriteAdapter(private val context: Context, private var userList: List<FavoriteUser>) :
        RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val user = userList[position]
            holder.bind(user)
        }

        override fun getItemCount(): Int = userList.size

        inner class ViewHolder(private val binding: ItemFavoriteBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(user: FavoriteUser) {
                Picasso.get().load(user.avatarUrl).into(binding.itemAvatar)
                binding.itemName.text = user.login
                binding.itemType.text = user.id.toString()
            }
        }

        fun updateDataFavorite(newList: List<FavoriteUser>) {
            userList = newList
        }
    }