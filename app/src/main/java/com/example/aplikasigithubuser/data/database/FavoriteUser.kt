package com.example.aplikasigithubuser.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteUser(
    @ColumnInfo(name = "login")
    val login: String,

    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String
) : Parcelable