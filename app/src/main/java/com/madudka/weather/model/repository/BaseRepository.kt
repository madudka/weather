package com.madudka.weather.model.repository

import com.madudka.weather.model.api.API
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseRepository<T>(val api: API) {
    val dataEmitter : BehaviorSubject<T> = BehaviorSubject.create()
}