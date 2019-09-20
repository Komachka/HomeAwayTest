package com.kstor.homeawaytest.view

abstract class BasePresentor<V>(protected var view: V?) {
    /*fun atachView(v: V) {
        view = v
    }*/

    fun detachView() {
        view = null

    }

}