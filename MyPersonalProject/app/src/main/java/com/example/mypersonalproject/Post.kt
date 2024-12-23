package com.example.mypersonalproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey val id: Int,
    val title: String,
    val body: String,
    var isFavorite: Boolean = false
)