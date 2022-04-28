package com.madudka.weather.view

import com.madudka.weather.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val FORMAT_DAY_MONTH_NAME = "dd MMMM"
const val FORMAT_DAY_WEEK_NAME = "dd EEEE"
const val FORMAT_HOUR_MINUTE = "HH:mm"

fun Long.toDateFormat(format: String) : String{
    val calendar = Calendar.getInstance()
    val timeZone = calendar.timeZone
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    simpleDateFormat.timeZone = timeZone
    return  simpleDateFormat.format(Date(this * 1000))
}

//fun Double.toCelsius() = (this - 273.15).roundToInt().toString() + "\u00B0"

fun Double.toCelsius() = (if ((this - 273.15).roundToInt() > 0) "+" else "") +
        (this - 273.15).roundToInt().toString() + "\u00B0"

//fun Double.toPercent(extraPart: String = "") = (this * 1000).roundToInt().toString() + extraPart

fun Double.toExtra(extraPart: String = "") = this.toString() + extraPart

fun Int.toMercuryMM() = String.format("%.2f", this * 0.750063755419211) + " мм"

fun Int.toExtra(extraPart: String = "") = this.toString() + extraPart


fun String.provideIcon() = when(this) {
    "01n", "01d" -> R.drawable.ic_01d
    "02n", "02d" -> R.drawable.ic_02d
    "03n", "03d" -> R.drawable.ic_03d
    "04n", "04d" -> R.drawable.ic_04d
    "09n", "09d" -> R.drawable.ic_09d
    "10n", "10d" -> R.drawable.ic_10d
    "11n", "11d" -> R.drawable.ic_11d
    "13n", "13d" -> R.drawable.ic_13d
    "50n", "50d" -> R.drawable.ic_50d
    else -> R.drawable.ic_error
}

fun String.provideImage() = when(this) {
    "01n", "01d" -> R.mipmap.clear_sky1x
    "02n", "02d" -> R.mipmap.few_clouds1x
    "03n", "03d" -> R.mipmap.scattered_clouds1x
    "04n", "04d" -> R.mipmap.broken_clouds1x
    "09n", "09d" -> R.mipmap.shower_rain1x
    "10n", "10d" -> R.mipmap.rain1x
    "11n", "11d" -> R.mipmap.thunderstorm1x
    "13n", "13d" -> R.mipmap.snow1x
    "50n", "50d" -> R.mipmap.mist1x
    else -> R.drawable.ic_error
}