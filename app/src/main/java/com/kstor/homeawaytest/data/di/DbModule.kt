package com.kstor.homeawaytest.data.di

import android.content.Context
import androidx.room.Room
import com.kstor.homeawaytest.data.db.AppDatabase
import com.kstor.homeawaytest.data.db.FavoritesDao
import com.kstor.homeawaytest.data.db.LocalData
import com.kstor.homeawaytest.data.db.VenuesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideRoomDb(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideVenuesDao(database: AppDatabase): VenuesDao {
        return database.venuesDao()
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(database: AppDatabase): FavoritesDao {
        return database.favoritesDao()
    }

    @Provides
    @Singleton
    fun provideLocalData(venuesDao: VenuesDao, favoritesDao: FavoritesDao): LocalData {
        return LocalData(venuesDao, favoritesDao)
    }
}
