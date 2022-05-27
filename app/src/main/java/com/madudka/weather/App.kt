package com.madudka.weather

import android.app.Application
import android.content.Intent
import com.madudka.weather.model.room.OpenWeatherDatabase
import com.madudka.weather.view.SettingsHolder

private const val SETTINGS = "SETTINGS"
private const val START_FlAG = "START_FLAG"
class App : Application() {

    companion object { lateinit var db : OpenWeatherDatabase }

    override fun onCreate() {
        super.onCreate()

        db = OpenWeatherDatabase.getInstance(applicationContext);

        val preferences = getSharedPreferences(SETTINGS, MODE_PRIVATE)

        SettingsHolder.onCreate(preferences)

        initAdYandex(applicationContext)

        val flag = preferences.contains(START_FlAG)

        if (!flag) {
            preferences.edit().putBoolean(START_FlAG, true).apply()

            val intent = Intent(this, InitActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}