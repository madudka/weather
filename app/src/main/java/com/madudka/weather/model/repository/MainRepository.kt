package com.madudka.weather.model.repository

import android.util.Log
import com.madudka.weather.model.WeatherDataModel
import com.madudka.weather.model.api.API
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

const val TAG = "MainRepository"

class MainRepository(api: API) : BaseRepository<MainRepository.ServerResponse>(api) {

    fun reloadData(lat: String, lon: String){
        //обеспечивает параллельное выполнение api.get...() в отдельных потоках
        //.zip(...) - раздваивает поток; .subscribeOn(Schedulers.io()) - создает отдельный поток
        Observable.zip(api.provideWeatherApi().getWeatherData(lat, lon)
            , api.provideGeoCodeApi().getLocationName(lat, lon).map{
                it.asSequence().map{ geoCodeModel -> geoCodeModel.name}
                    .toList()
                    .filterNotNull()
                    .first()
            }
        ) { weatherData, geoCode -> ServerResponse(geoCode, weatherData) }
            .subscribeOn(Schedulers.io())
            .doOnNext { //TODO "Добавление объекта в БД"
            }
            //.onErrorResumeNext { TODO "Получение объекта из БД" }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                       dataEmitter.onNext(it)
            },{
                Log.d(TAG, "reloadData: $it")
            })
    }

    data class ServerResponse(val city: String, val weatherDataModel: WeatherDataModel
        , val error: Throwable? = null)
}