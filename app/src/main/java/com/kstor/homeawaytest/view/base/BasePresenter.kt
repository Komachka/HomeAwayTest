package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.view.utils.TestSchedulerProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V> constructor(val compositeDisposable: CompositeDisposable,
                                            val schedulerProvider: TestSchedulerProvider) {
    var view: V? = null

    fun attachView(v: V) {
        view = v
    }

    fun detachView() {
        compositeDisposable.clear()
        view = null
    }
}
