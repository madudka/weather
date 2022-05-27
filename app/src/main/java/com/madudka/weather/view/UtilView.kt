package com.madudka.weather.view

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.madudka.weather.R
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import java.text.SimpleDateFormat
import java.util.*

const val FORMAT_DAY_MONTH_NAME = "dd MMMM"
const val FORMAT_DAY_WEEK_NAME = "dd EEEE"
const val FORMAT_HOUR_MINUTE = "HH:mm"

fun Long.toDateFormat(format: String) : String{
    val calendar = Calendar.getInstance()
    val timeZone = calendar.timeZone
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    simpleDateFormat.timeZone = timeZone
    return simpleDateFormat.format(Date(this * 1000))
}

fun Double.toDegree() = SettingsHolder.temp.getValue(this)

fun Double.toExtra(extraPart: String = "") = this.toString() + extraPart

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

private abstract class CustomTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

    }
}

fun TextInputEditText.createObservable() : Flowable<String>{
    return Flowable.create({
        addTextChangedListener(object: CustomTextWatcher(){
            override fun afterTextChanged(s: Editable?) {
                it.onNext(s.toString())
            }
        })
    }, BackpressureStrategy.BUFFER)
}

enum class State {
    CURRENT, SAVED
}