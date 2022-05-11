package com.madudka.weather.model.repository

import com.madudka.weather.model.GeoCodeModel
import com.madudka.weather.model.api.API
import com.madudka.weather.model.mapToEntity
import com.madudka.weather.model.mapToModel
import com.madudka.weather.view.State
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LocationRepository(api: API) : BaseRepository<LocationRepository.DataLoc>(api) {

    private val dbAccess = db.getGeoCodeDao()

    fun getLocations(name: String){
        api.provideGeoCodeApi().getLocationName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                DataLoc(it, State.CURRENT)
            }
            .subscribe {
                dataEmitter.onNext(it)
            }
    }

    fun add(data: GeoCodeModel){
        getFavoriteList{ dbAccess.insertGeoCode(data.mapToEntity()) }
    }

    fun remove(data: GeoCodeModel){
        getFavoriteList{ dbAccess.deleteGeoCode(data.mapToEntity()) }
    }

    fun updateFavorite(){
        getFavoriteList()
    }

    private fun getFavoriteList(query : (() -> Unit)? = null){
        roomTransaction {
            query?.let { it() }
            DataLoc(dbAccess.selectAllGeoCode().map { it.mapToModel() }, State.SAVED)
        }
    }

    data class DataLoc(val data: List<GeoCodeModel>, val type: State)

}