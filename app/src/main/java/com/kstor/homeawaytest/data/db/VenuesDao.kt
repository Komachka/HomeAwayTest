package com.kstor.homeawaytest.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kstor.homeawaytest.data.db.model.DBVenuesModel
import io.reactivex.Single

@Dao
interface VenuesDao {
    @Query("SELECT * FROM venues")
    fun getAll(): Single<List<DBVenuesModel>>

    @Query("SELECT * FROM venues WHERE id IN (:venuesId)")
    fun loadAllByIds(venuesId: IntArray): List<DBVenuesModel>

    @Query("SELECT * FROM venues WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): DBVenuesModel

    @Insert
    fun insertAll(vararg users: DBVenuesModel)

    @Delete
    fun delete(user: DBVenuesModel)
}
