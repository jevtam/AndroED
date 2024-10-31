package com.example.gson

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchData()
    }

    private fun fetchData() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Timber.e("Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val responseBody = response.body?.string()
                val wrapper = gson.fromJson(responseBody, Wrapper::class.java)

                val photoLinks = wrapper.photos.photo.map { photo ->
                    "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_z.jpg"
                }

                runOnUiThread {
                    val recyclerView = findViewById<RecyclerView>(R.id.rView)
                    recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)
                    recyclerView.adapter = PhotoAdapter(photoLinks, this@MainActivity)
                }
            }
        })
    }
}