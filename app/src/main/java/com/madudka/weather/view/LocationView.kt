package com.madudka.weather.view

import com.madudka.weather.model.GeoCodeModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface LocationView : MvpView
{
    @AddToEndSingle
    fun fillResultList(data: List<GeoCodeModel>)

    @AddToEndSingle
    fun fillFavoriteList(data: List<GeoCodeModel>)

    @AddToEndSingle
    fun setLoading(flag: Boolean)
}