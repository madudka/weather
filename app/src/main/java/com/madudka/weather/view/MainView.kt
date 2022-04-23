package com.madudka.weather.view

import com.madudka.weather.model.DayModel
import com.madudka.weather.model.HourModel
import com.madudka.weather.model.WeatherData
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView : MvpView {

    @AddToEndSingle
    fun showCity(data: String)

    @AddToEndSingle
    fun showCurrentData(data: WeatherData)

    @AddToEndSingle
    fun showHourData(data: List<HourModel>)

    @AddToEndSingle
    fun showDayData(data: List<DayModel>)

    @AddToEndSingle
    fun showError(error: Throwable)

    @AddToEndSingle
    fun setLoading(flag: Boolean)
}