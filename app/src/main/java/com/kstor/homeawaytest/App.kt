package com.kstor.homeawaytest

import android.app.Application
import com.kstor.homeawaytest.data.di.NetworkModule
import com.kstor.homeawaytest.data.di.RepositoryModule
import com.kstor.homeawaytest.data.di.SharedPrefModule
import com.kstor.homeawaytest.view.di.AppComponent
import com.kstor.homeawaytest.view.di.AppModule
import com.kstor.homeawaytest.view.di.DaggerAppComponent

class App : Application() {

    lateinit var homeAwayComponents: AppComponent

    override fun onCreate() {
        super.onCreate()
        homeAwayComponents = init()
    }

    private fun init(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .repositoryModule(RepositoryModule())
            .sharedPrefModule(SharedPrefModule())
            .build()
    }
}
