package com.example.weatherapplication

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("search")
    fun getCoordinates(
        @Query("q") cityName: String,
        @Query("api_key") apiKey: String
    ): Call<List<Result>> // This should return a list of Result objects
}

data class GeocodingResponse(val results: List<Result>)
data class Result(
    val place_id: Long,
    val lat: String,
    val lon: String,
    val display_name: String,
    val type: String,
    @SerializedName("class") val placeClass: String
)
