package com.example.mypersonalproject

import androidx.room.*
import com.example.mypersonalproject.models.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAllPosts(): List<Post>

    @Query("SELECT * FROM posts WHERE isFavorite = 1")
    fun getFavoritePosts(): List<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<Post>)

    @Update
    fun updatePost(post: Post)
}