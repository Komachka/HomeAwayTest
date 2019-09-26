package com.kstor.homeawaytest.view

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V>(val compositeDisposable: CompositeDisposable) {
    var view: V? = null

    fun attachView(v: V) {
        view = v
    }

    fun detachView() {
        compositeDisposable.clear()
        view = null
    }
}
