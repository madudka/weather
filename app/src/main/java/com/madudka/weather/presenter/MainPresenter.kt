package com.madudka.weather.presenter

import android.util.Log
import com.madudka.weather.model.api.API
import com.madudka.weather.model.repository.MainRepository
import com.madudka.weather.view.MainView

class MainPresenter() : BasePresenter<MainView>() {

    private val repository = MainRepository(API())

    override fun enable() {
        repository.dataEmitter.subscribe { res ->
            Log.d("MainRepository", "enable(): $res")
            viewState.showLocation(res.city)
            viewState.showDayData(res.weatherDataModel.daily)
            viewState.showHourData(res.weatherDataModel.hourly)
            viewState.showCurrentData(res.weatherDataModel)
            res.error?.let { viewState.showError(res.error) }
        }
    }

    fun refresh(lat: String, lon: String){
        viewState.setLoading(true)
        repository.reloadData(lat, lon)
    }
}