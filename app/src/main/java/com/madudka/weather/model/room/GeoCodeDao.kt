package com.madudka.weather.model.room

import androidx.room.*

@Dao
interface GeoCodeDao {

    @Query("SELECT * FROM GeoCode")
    fun selectAllGeoCode() : List<GeoCodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeoCode(data: GeoCodeEntity)

    @Delete
    fun deleteGeoCode(data: GeoCodeEntity)
}