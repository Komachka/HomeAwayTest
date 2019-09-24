package com.kstor.homeawaytest.view

abstract class BasePresentor<V> {
    var view: V? = null

    fun attachView(v: V) {
        view = v
    }

    fun detachView() {
        view = null
    }




}
