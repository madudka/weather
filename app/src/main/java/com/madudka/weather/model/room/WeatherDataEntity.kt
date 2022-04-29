package com.madudka.weather.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WeatherData", )
data class WeatherDataEntity(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "data")
    val data: String
)
