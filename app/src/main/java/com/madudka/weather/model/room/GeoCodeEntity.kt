package com.madudka.weather.model.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.madudka.weather.model.LocalNames

@Entity(tableName = "GeoCode", primaryKeys = ["lat", "lon"])
data class GeoCodeEntity (
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "lat")
    val lat: Double,
    @Embedded
    val local_names: LocalNames,
    @ColumnInfo(name = "lon")
    val lon: Double,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "state")
    val state: String,
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)