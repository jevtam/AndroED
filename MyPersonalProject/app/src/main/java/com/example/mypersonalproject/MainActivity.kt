package com.example.mypersonalproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypersonalproject.api.RetrofitClient
import com.example.mypersonalproject.databinding.ActivityMainBinding
import com.example.mypersonalproject.models.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import PostsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostsAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostsAdapter(
            onFavoriteClicked = { post ->
                toggleFavorite(post)
            },
            onFetchPosts = { post ->
                lifecycleScope.launch(Dispatchers.IO) {
                    database.postDao().updatePost(post)
                }
            }
        )
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterPosts(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterPosts(it)
                }
                return true
            }
        })

        fetchPosts()
    }

    private fun filterPosts(query: String) {
        lifecycleScope.launch {
            val filteredList = withContext(Dispatchers.IO) {
                database.postDao().getAllPosts().filter {
                    it.title.contains(query, ignoreCase = true) || it.body.contains(query, ignoreCase = true)
                }
            }
            adapter.submitList(filteredList)
        }
    }

    private fun fetchPosts() {
        val postsFromDb = database.postDao().getAllPosts()

        if (postsFromDb.isNotEmpty()) {
            adapter.submitList(postsFromDb)
        } else {
            RetrofitClient.instance.getPosts().enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { posts ->
                            adapter.submitList(posts)
                            database.postDao().insertPosts(posts)
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun toggleFavorite(post: Post) {
        post.isFavorite = !post.isFavorite
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                database.postDao().updatePost(post)
            }
            adapter.notifyItemChanged(adapter.currentList.indexOf(post))
        }
    }
}