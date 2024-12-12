package com.example.retrofitforecaster

data class WeatherResponse(
    val list: List<WeatherItem>
)

data class WeatherItem(
    val main: Main,
    val weather: List<Weather>,
    val dt_txt: String
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String
)