package com.madudka.weather.model.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/onecall?")
    fun getWeatherData(
        @Query("lat") lat : String,
        @Query("lon") lon : String,
        @Query("exclude") exclude : String = "minutely, alerts",
        @Query("appid") appid : String = "59c258ce19c4588d773f38bb7df92013",
        @Query("lang") lang : String = "en"
    )
}