package com.madudka.weather.presenter

import com.madudka.weather.view.MainView

class MainPresenter : BasePresenter<MainView>() {

    //TODO переменная репозитория

    override fun enable() {
        //TODO("Not yet implemented")
    }

    fun refresh(lat: String, lon: String){
        viewState.setLoading(true)
        //TODO обращение к репозиторию
    }
}