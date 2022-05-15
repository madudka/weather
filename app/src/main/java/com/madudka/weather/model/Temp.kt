package com.madudka.weather.model

data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
){
    fun getAvg()= (morn + day + eve + night)/4
}