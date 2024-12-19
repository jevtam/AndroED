package com.example.retrofitforecaster

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitforecaster.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import com.example.retrofitforecaster.BuildConfig
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = WeatherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rView.layoutManager = LinearLayoutManager(this)
        binding.rView.adapter = adapter

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        if (WeatherStore.weathers != null) {
            adapter.submitList(WeatherStore.weathers)
            Timber.d("Restored data from WeatherStore")
        } else {
            fetchWeatherData()
        }
    }

    private fun fetchWeatherData() {
        RetrofitClient.instance.getForecast("Shklow", RetrofitClient.getApiKey()).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherList = response.body()?.list
                    WeatherStore.weathers = weatherList
                    adapter.submitList(weatherList)
                    Timber.d("Fetched data: $weatherList")
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val gson = Gson()
        val weatherJson = gson.toJson(adapter.currentList)
        outState.putString("WEATHER_DATA", weatherJson)
        Timber.d("onSaveInstanceState: Saved data - $weatherJson")
    }
}