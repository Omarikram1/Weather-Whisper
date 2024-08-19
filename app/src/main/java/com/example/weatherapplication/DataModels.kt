package com.example.weatherapplication

data class Weather(
    val icon: String,
    val code: Int,
    val description: String
)

data class WeatherData(
    val lon: Double,
    val lat: Double,
    val city_name: String,
    val temp: Double,
    val temp_min: Double?,         // Added Minimum Temperature (Optional)
    val temp_max: Double?,
    val rh: Int,
    val wind_spd: Double,
    val wind_cdir_full: String,
    val weather: Weather,
    val sunrise: String,
    val sunset: String,
    val aqi: Int,
    val datetime: String
)

data class WeatherResponse(
    val data: List<WeatherData>
)