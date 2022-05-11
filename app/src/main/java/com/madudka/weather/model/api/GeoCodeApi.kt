package com.madudka.weather.model.api

import com.madudka.weather.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodeApi {
    @GET("geo/1.0/direct")
    fun getLocationName(
        @Query("q") name : String,
        @Query("limit") limit : String = "10",
        @Query("appid") appid : String = "59c258ce19c4588d773f38bb7df92013"
    ) : Observable<List<GeoCodeModel>>

    @GET("geo/1.0/reverse?")
    fun getLocationCord(
        @Query("lat") lat : String,
        @Query("lon") lon : String,
        @Query("limit") limit : String = "10",
        @Query("appid") appid : String = "59c258ce19c4588d773f38bb7df92013"
    ) : Observable<List<GeoCodeModel>>
}

//lat={lat}&lon={lon}&limit={limit}&appid={API key}