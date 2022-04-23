package com.madudka.weather

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences

private const val SETTINGS = "SETTINGS"
private const val START_FlAG = "START_FLAG"
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val preferences = getSharedPreferences(SETTINGS, MODE_PRIVATE)
        val flag = preferences.contains(START_FlAG)

        if (!flag) {
            preferences.edit().putBoolean(START_FlAG, true).apply()

            val intent = Intent(this, InitActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }
}