package com.madudka.weather.model.repository


import android.util.Log
import com.google.gson.Gson
import com.madudka.weather.model.WeatherDataModel
import com.madudka.weather.model.api.API
import com.madudka.weather.model.room.WeatherDataEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

const val TAG = "MainRepository"

class MainRepository(api: API) : BaseRepository<MainRepository.ServerResponse>(api) {

    private val gson = Gson()
    private val dbAccess = db.getWeatherDataDao()

    fun reloadData(lat: String, lon: String){
        //обеспечивает параллельное выполнение api.get...() в отдельных потоках
        //.zip(...) - раздваивает поток; .subscribeOn(Schedulers.io()) - создает отдельный поток
        Observable.zip(api.provideWeatherApi().getWeatherData(lat, lon)
            , api.provideGeoCodeApi().getLocationName(lat, lon).map{
                it.asSequence().map{ geoCodeModel -> geoCodeModel.name}
                    .toList()
                        //TODO Локализация
                    .filterNotNull()
                    .first()
            }
        ) { weatherDataModel, geoCodeModel -> ServerResponse(geoCodeModel, weatherDataModel) }
            .subscribeOn(Schedulers.io())
            .doOnNext { dbAccess.insertWeatherData(
                WeatherDataEntity(data = gson.toJson(it.weatherDataModel), location = it.city))
            }
            .onErrorResumeNext {
                Observable.just(ServerResponse(
                    dbAccess.selectWeatherData().location,
                gson.fromJson(dbAccess.selectWeatherData().data, WeatherDataModel::class.java),
                it))
            }
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