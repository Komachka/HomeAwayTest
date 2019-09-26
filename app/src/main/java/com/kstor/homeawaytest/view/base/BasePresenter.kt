package com.kstor.homeawaytest.view.base

import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BasePresenter<V> constructor(val compositeDisposable: CompositeDisposable) {
    var view: V? = null

    fun attachView(v: V) {
        view = v
    }

    fun detachView() {
        compositeDisposable.clear()
        view = null
    }
}
