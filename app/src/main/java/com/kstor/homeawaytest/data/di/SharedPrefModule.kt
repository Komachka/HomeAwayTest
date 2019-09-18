package com.kstor.homeawaytest.data.di

import android.content.Context
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefModule {

    @Provides
    @Singleton
    fun provideSharedPrefDara(context: Context)  =  SharedPreferenceData(context)
}