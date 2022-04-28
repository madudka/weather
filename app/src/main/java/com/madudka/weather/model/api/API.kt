package com.madudka.weather.model.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class API {

    private val openWeatherMap: Retrofit by lazy { initAPI() }

    private fun initAPI() = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.openweathermap.org/")
        .build()

    fun provideWeatherApi() : WeatherApi = openWeatherMap.create(WeatherApi::class.java)

    fun provideGeoCodeApi() : GeoCodeApi = openWeatherMap.create(GeoCodeApi::class.java)
}