package com.madudka.weather.model.room

import androidx.room.*

@Dao
interface WeatherDataDao {

    @Query("SELECT * FROM WeatherData WHERE id = 1")
    fun selectWeatherData() : WeatherDataEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(data: WeatherDataEntity)

    @Update
    fun updateWeatherData(data: WeatherDataEntity)

    @Delete
    fun deleteWeatherData(data: WeatherDataEntity)
}