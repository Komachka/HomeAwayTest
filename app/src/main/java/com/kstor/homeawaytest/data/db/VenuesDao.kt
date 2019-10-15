package com.kstor.homeawaytest.data.db

import androidx.room.*
import com.kstor.homeawaytest.data.VENUES_TABLE_NAME
import com.kstor.homeawaytest.data.db.model.DBVenuesModel

@Dao
interface VenuesDao {
    @Query("SELECT * FROM $VENUES_TABLE_NAME")
    suspend fun getAll(): List<DBVenuesModel>

    @Query("SELECT * FROM $VENUES_TABLE_NAME WHERE id IN (:venuesId)")
    suspend fun loadAllByIds(venuesId: IntArray): List<DBVenuesModel>

    @Query("SELECT * FROM $VENUES_TABLE_NAME WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): DBVenuesModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg venues: DBVenuesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(venues: List<DBVenuesModel>)

    @Delete
    suspend fun delete(venues: DBVenuesModel)

    @Query("DELETE FROM $VENUES_TABLE_NAME")
    suspend fun deleteAllVenues()

    @Transaction
    suspend fun deleteAndSave(venues: List<DBVenuesModel>) {
        deleteAllVenues()
        insertAll(venues)
    }
}
