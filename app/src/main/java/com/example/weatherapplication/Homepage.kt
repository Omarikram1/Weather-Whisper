package com.example.weatherapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapplication.databinding.HomePageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Homepage : AppCompatActivity() {
    private lateinit var binding: HomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWeatherData(31.5204,74.3587)
        binding.searchIcon.setOnClickListener {
            val city = binding.searchEditText.text.toString().trim()

            if (city.isNotEmpty()) {
                Log.d("Homepage", "Searching for city: $city")
                fetchCoordinates(city)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCoordinates(cityName: String) {
        val apiKey = "66bb3d736373f930065963jgq090f80"
        Log.d("Homepage", "Searching for city0:------------------------------------------")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://geocode.maps.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApi::class.java)

        Log.d("Homepage", "Fetching coordinates for: $cityName")
        Log.d("Homepage", "Searching for city1:------------------------------------------")
        val call = retrofit.getCoordinates(cityName, apiKey)
        Log.d("Homepage", "Searching for city2:-------------------------------")
        call.enqueue(object : Callback<List<Result>> {
            override fun onResponse(call: Call<List<Result>>, response: Response<List<Result>>) {
                if (response.isSuccessful && response.body() != null) {
                    val cityResult = response.body()?.firstOrNull { it.type == "city" || it.placeClass == "boundary"}
                    if (cityResult != null) {
                        Log.d("Homepage", "Latitude: ${cityResult.lat}, Longitude: ${cityResult.lon}")
                        getWeatherData(cityResult.lat.toDouble(), cityResult.lon.toDouble())
                    } else {
                        Toast.makeText(this@Homepage, "Not a valid city name", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("Homepage", "Geocoding failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Result>>, t: Throwable) {
                Toast.makeText(this@Homepage, "API call failed: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.e("Homepage", "Geocode API call failed", t)
            }
        })
    }

    private fun getWeatherData(lat: Double, lon: Double) {
        val apiKey = "b997112a2c124807b8d9eb8df127ebc3"
        val baseURL = "https://api.weatherbit.io/v2.0/"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

        Log.d("Homepage", "Fetching weather for coordinates: Latitude: $lat, Longitude: $lon")
        val call = retrofit.getCurrentWeather(lat, lon, apiKey)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                Log.d("Homepage", "Weather data response received")
                if (response.isSuccessful) {
                    response.body()?.data?.forEach { weatherData ->
                        Log.d("Homepage", "City: ${weatherData.city_name}")
                        Log.d("Homepage", "Min Temp: ${weatherData.temp_min ?: "N/A"}°C")
                        Log.d("Homepage", "Max Temp: ${weatherData.temp_max ?: "N/A"}°C")
                        Log.d("Homepage", "Date: ${weatherData.datetime}")
                        Log.d("Homepage", "Temperature in ${weatherData.city_name}: ${weatherData.temp}")
                        Log.d("Homepage", "Humidity: ${weatherData.rh}%")
                        Log.d("Homepage", "Wind Speed: ${weatherData.wind_spd} m/s")
                        Log.d("Homepage", "Wind Direction: ${weatherData.wind_cdir_full}")
                        Log.d("Homepage", "Condition: ${weatherData.weather.description}")
                        Log.d("Homepage", "Sunrise: ${weatherData.sunrise}")
                        Log.d("Homepage", "Sunset: ${weatherData.sunset}")
                        Log.d("Homepage", "Air Quality Index: ${weatherData.aqi}")
                        val weatherCondition = response.body()?.data?.firstOrNull()?.weather?.description
                        if (weatherCondition != null) {
                            Log.d("Homepage", "Weather condition: $weatherCondition")

                            binding.Conditiontext.text = weatherCondition.uppercase()
                        }
//                        val date = weatherData.datetime.substring(0,10)

                        // Update your UI elements here
                        val backgroundDrawable = when (weatherCondition) {
                            "Clear sky" -> R.drawable.sunny2
                            "Light rain" -> R.drawable.rainy
                            "Heavy rain" -> R.drawable.rainy
                            "Moderate Rain" -> R.drawable.rainy
                            "Rain" -> R.drawable.rainy
                            "Scattered clouds" -> R.drawable.cloudyy
                            "Clouds" -> R.drawable.cloudyy
                            "Clear" -> R.drawable.sunny2
                            "Smoke" -> R.drawable.sunny2
                            "Broken clouds" -> R.drawable.cloudyy
                            "Overcast clouds" -> R.drawable.cloudyy
                            "Drizzle" -> R.drawable.rainy
                            // Add more conditions as needed
                            else -> R.drawable.sunny2 // A default background if condition is not found
                        }
                        val animationResource = when (weatherCondition) {
                            "Clear sky" -> R.raw.animationsunny
                            "Light rain" -> R.raw.animationrainy
                            "Heavy rain" -> R.raw.animationrainy
                            "Moderate Rain" -> R.raw.animationrainy
                            "Rain" -> R.raw.animationrainy
                            "Scattered clouds" -> R.raw.animationcloudy
                            "Clouds" -> R.raw.animationcloudy
                            "Clear" -> R.raw.animationsunny
                            "Smoke" -> R.raw.animationsunny
                            "Broken clouds" -> R.raw.animationcloudy
                            "Overcast clouds" -> R.raw.animationcloudy
                            "Drizzle" -> R.raw.animationrainy
                            // Add more conditions as needed
                            else -> R.raw.animationsunny // A default animation if condition is not found
                        }
                        val lottieView = binding.imageDisplay
                        lottieView.setAnimation(animationResource)
                        lottieView.playAnimation()
                        val mainnLayout = binding.mainlayout // Ensure your RelativeLayout has an id like "mainLayout"
                        mainnLayout.setBackgroundResource(backgroundDrawable)

                        binding.CityName.text = weatherData.city_name
                        binding.temperatureText.text = "${weatherData.temp.toInt()}°"

                        binding.dateText.text = weatherData.datetime.substring(0,10)
                        binding.HumidityValue.text = "${weatherData.rh}%"
                        binding.WindValue.text = "${weatherData.wind_spd} m/s, ${weatherData.wind_cdir_full}"
                        binding.WeatherValue.text = weatherData.weather.description
                        binding.SunriseValue.text = weatherData.sunrise
                        binding.SunsetValue.text = weatherData.sunset
                        binding.AQIValue.text = weatherData.aqi.toString()
                    }
                } else {
                    Log.d("Homepage", "Response not successful: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("Homepage", "Weather API call failed", t)
            }
        })
    }

}
