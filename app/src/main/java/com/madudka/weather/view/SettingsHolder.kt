package com.madudka.weather.view

import android.content.SharedPreferences
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.madudka.weather.R
import java.util.*
import kotlin.math.roundToInt

const val TEMPERATURE = "TEMPERATURE"
const val WIND_SPEED = "WIND_SPEED"
const val PRESSURE = "PRESSURE"

object SettingsHolder {

    const val C = 1;
    const val F = 2;
    const val MS = 3;
    const val KMH = 4;
    const val MMHG = 5;
    const val HPA = 6;

    private lateinit var preferences: SharedPreferences
    var temp = Setting.TEMP_C
    var ws = Setting.WS_MS
    var pres = Setting.PRES_MMHG

    fun onCreate(pref: SharedPreferences){
        preferences = pref
        temp = getSetting(preferences.getInt(TEMPERATURE, C))
        ws = getSetting(preferences.getInt(WIND_SPEED, MS))
        pres = getSetting(preferences.getInt(PRESSURE, HPA))
    }

    fun onDestroy(){
        val editor = preferences.edit()
        editor.apply {
            putInt(TEMPERATURE, temp.prefConst)
            putInt(WIND_SPEED, ws.prefConst)
            putInt(PRESSURE, pres.prefConst)
        }.apply()
    }

    private fun getSetting(id: Int) = when (id) {
        C -> Setting.TEMP_C
        F -> Setting.TEMP_F
        MS -> Setting.WS_MS
        KMH -> Setting.WS_KMH
        MMHG -> Setting.PRES_MMHG
        HPA -> Setting.PRES_HPA
        else -> throw InputMismatchException()
    }



    enum class Setting(@IdRes val checkedViewId: Int, @StringRes val unitStringRes: Int, val prefConst: Int){

        TEMP_C(R.id.temp_unit_c, R.string.temp_c, C){
            override fun getValue(v: Double) = (if (v - 273.15 > 0) "+" else "") +
                    (v - 273.15).roundToInt().toString() + "\u00B0"
        },
        TEMP_F(R.id.temp_unit_f, R.string.temp_f, F){
            override fun getValue(v: Double) = (if ((v - 273.15) * 1.8 + 32 > 0) "+" else "") +
                    ((v - 273.15) * 1.8 + 32).roundToInt().toString() + "\u00B0"
        },
        WS_MS(R.id.wind_speed_unit_ms, R.string.wind_speed_ms, MS){
            override fun getValue(v: Double) = v.roundToInt().toString()
        },
        WS_KMH(R.id.wind_speed_unit_kmh, R.string.wind_speed_kmh, KMH){
            override fun getValue(v: Double) = (v * 3.6).roundToInt().toString()
        },
        PRES_MMHG(R.id.pres_unit_mmHg, R.string.pressure_mmHg, MMHG){
            override fun getValue(v: Double) = (v * 0.750063755419211).roundToInt().toString()
        },
        PRES_HPA(R.id.pres_unit_hpa, R.string.pressure_hpa, HPA){
            override fun getValue(v: Double) = v.roundToInt().toString()
        };

        abstract fun getValue(v: Double) : String
    }
}