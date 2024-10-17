package com.example.myapplication2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.Button
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.concurrent.thread
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnHTTP = findViewById<Button>(R.id.btnHTTP)
        val btnOkHTTP = findViewById<Button>(R.id.btnOkHTTP) // Новый button

        // Обработчик для кнопки btnHTTP (старый обработчик)
        btnHTTP.setOnClickListener {
            // Ваш старый код для btnHTTP
        }

        // Обработчик для кнопки btnOkHTTP (новый)
        btnOkHTTP.setOnClickListener {
            // Создаем OkHttpClient
            val client = OkHttpClient()

            // Создаем запрос
            val request = Request.Builder()
                .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
                .build()

            // Выполняем асинхронный запрос
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Логируем ошибку
                    Log.e("Flickr OkCats", "Request failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    // Логируем ответ на уровне INFO
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        Log.i("Flickr OkCats", responseBody ?: "Empty Response")
                    } else {
                        Log.e("Flickr OkCats", "Unexpected response: ${response.message}")
                    }
                }
            })
        }
    }
}