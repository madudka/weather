package com.madudka.weather.model

data class WeatherDataModel(
    val current: Current,
    val daily: List<DayModel>,
    val hourly: List<HourModel>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)