package com.madudka.weather.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WeatherDataEntity::class], exportSchema = false, version = 1)
abstract class OpenWeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDataDao() : WeatherDataDao

    //TODO К релизу убрать .fallbackToDestructiveMigration()
    companion object : SingletonHolder<OpenWeatherDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, OpenWeatherDatabase::class.java, "OpenWeatherDatabase")
            .fallbackToDestructiveMigration()
            .build()
    })
}