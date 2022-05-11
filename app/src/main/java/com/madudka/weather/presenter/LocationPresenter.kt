package com.madudka.weather.presenter

import com.madudka.weather.model.GeoCodeModel
import com.madudka.weather.model.api.API
import com.madudka.weather.model.repository.LocationRepository
import com.madudka.weather.view.LocationView
import com.madudka.weather.view.State

class LocationPresenter() : BasePresenter<LocationView>(){

    private val repository = LocationRepository(API())
    override fun enable() {

        repository.dataEmitter.subscribe {
            viewState.setLoading(false)
            if (it.type == State.SAVED) viewState.fillFavoriteList(it.data)
            else viewState.fillResultList(it.data)
        }
    }

    fun search(s: String){
        repository.getLocations(s)
    }

    fun remove(data: GeoCodeModel){
        repository.remove(data)
    }

    fun save(data: GeoCodeModel){
        repository.add(data)
    }

    fun getFavoriteList(){
        repository.updateFavorite()
    }

}