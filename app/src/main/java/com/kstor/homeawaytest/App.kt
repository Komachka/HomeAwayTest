package com.kstor.homeawaytest

import android.app.Application
import com.kstor.homeawaytest.data.di.NetworkModule
import com.kstor.homeawaytest.data.di.RepositoryModule
import com.kstor.homeawaytest.view.AppModule

class App : Application() {

    lateinit var homeAwayComponents: AppComponent

    override fun onCreate() {
        super.onCreate()
        homeAwayComponents = init(this)
    }

    private fun init(app: App): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .repositoryModule(RepositoryModule())
            .build()
    }
}
