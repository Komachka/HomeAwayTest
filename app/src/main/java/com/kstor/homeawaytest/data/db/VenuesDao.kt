package com.kstor.homeawaytest.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kstor.homeawaytest.data.VENUES_TABLE_NAME
import com.kstor.homeawaytest.data.db.model.DBVenuesModel
import io.reactivex.Single

@Dao
interface VenuesDao {
    @Query("SELECT * FROM $VENUES_TABLE_NAME")
    fun getAll(): Single<List<DBVenuesModel>>

    @Query("SELECT * FROM $VENUES_TABLE_NAME WHERE id IN (:venuesId)")
    fun loadAllByIds(venuesId: IntArray): List<DBVenuesModel>

    @Query("SELECT * FROM $VENUES_TABLE_NAME WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): DBVenuesModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg venues: DBVenuesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(venues: List<DBVenuesModel>)

    @Delete
    fun delete(venues: DBVenuesModel)

    @Query("DELETE FROM $VENUES_TABLE_NAME")
    fun deleteAllVenues()

    @Transaction
    fun deleteAndSave(venues: List<DBVenuesModel>) {
        deleteAllVenues()
        insertAll(venues)
    }
}
