package com.example.gson

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private val photoUrls = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchPhotos()
    }

    private fun fetchPhotos() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    val gson = Gson()
                    val wrapper = gson.fromJson(responseBody, Wrapper::class.java)
                    val photos = wrapper.photos.photo

                    photoUrls.clear()
                    photoUrls.addAll(photos.map { photo ->
                        "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_z.jpg"
                    })

                    runOnUiThread {
                        photoAdapter = PhotoAdapter(photoUrls) { photoUrl ->
                            val intent = Intent(this@MainActivity, PicViewer::class.java)
                            intent.putExtra("picUrl", photoUrl)
                            startActivity(intent)
                        }
                        recyclerView.adapter = photoAdapter
                    }
                }
            }
        })
    }
}
