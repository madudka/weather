package com.madudka.weather.model

import com.madudka.weather.model.room.GeoCodeEntity

fun GeoCodeModel.mapToEntity() = GeoCodeEntity(
    name = this.name,
    lat = this.lat,
    local_names = this.local_names,
    lon = this.lon,
    country = this.country,
    state = this.state ?: "",
    isFavorite = this.isFavorite
)

fun GeoCodeEntity.mapToModel() = GeoCodeModel(
    name = this.name,
    lat = this.lat,
    local_names = this.local_names,
    lon = this.lon,
    country = this.country,
    state = this.state,
    isFavorite = this.isFavorite
)