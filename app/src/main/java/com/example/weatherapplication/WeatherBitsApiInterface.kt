package com.example.weatherapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherService {
    @GET("current")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") apiKey: String,
        @Query("include") include: String? = null
    ): Call<WeatherResponse>
}
