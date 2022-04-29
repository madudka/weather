package com.madudka.weather.model.repository

import com.madudka.weather.App
import com.madudka.weather.model.api.API
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseRepository<T>(val api: API) {

    val dataEmitter : BehaviorSubject<T> = BehaviorSubject.create()
    protected val db by lazy { App.db }
    protected fun roomTransaction(tran: () -> T) = Observable.fromCallable { tran() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { dataEmitter.onNext(it) }
}