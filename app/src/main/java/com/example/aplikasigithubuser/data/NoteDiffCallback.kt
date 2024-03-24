package com.example.aplikasigithubuser.data

import androidx.recyclerview.widget.DiffUtil
import com.example.aplikasigithubuser.data.database.FavoriteUser

class NoteDiffCallback(private val oldNoteList: List<FavoriteUser>, private val newNoteList: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldNoteList.size
    override fun getNewListSize(): Int = newNoteList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition].id == newNoteList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldNoteList[oldItemPosition]
        val newNote = newNoteList[newItemPosition]
        return oldNote.login == newNote.login && oldNote.avatarUrl == newNote.avatarUrl
    }
}