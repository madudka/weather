package com.madudka.weather.view

import com.madudka.weather.model.DayModel
import com.madudka.weather.model.HourModel
import com.madudka.weather.model.WeatherDataModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView : MvpView {

    @AddToEndSingle
    fun showLocation(data: String)

    @AddToEndSingle
    fun showCurrentData(data: WeatherDataModel)

    @AddToEndSingle
    fun showHourData(data: List<HourModel>)

    @AddToEndSingle
    fun showDayData(data: List<DayModel>)

    @AddToEndSingle
    fun showError(error: Throwable)

    @AddToEndSingle
    fun setLoading(flag: Boolean)
}