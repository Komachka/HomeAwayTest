package com.kstor.homeawaytest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kstor.homeawaytest.data.db.model.DBVenuesModel

@Database(entities = [DBVenuesModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun venuesDao(): VenuesDao
}
