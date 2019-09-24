package com.kstor.homeawaytest.data.di

import android.content.Context
import android.content.SharedPreferences
import com.kstor.homeawaytest.data.PERSISTENT_STORAGE_NAME
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefModule {

    @Provides
    @Singleton
    fun provideSharedPrefData(preferences: SharedPreferences): SharedPreferenceData {
        return SharedPreferenceData(preferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(
        PERSISTENT_STORAGE_NAME,
        Context.MODE_PRIVATE)
    }
}
