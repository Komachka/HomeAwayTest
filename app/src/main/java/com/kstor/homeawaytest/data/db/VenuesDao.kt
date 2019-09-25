package com.kstor.homeawaytest.data.db

import androidx.room.*
import com.kstor.homeawaytest.data.db.model.DBFavoriteModel
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg venues: DBVenuesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(venues: List<DBVenuesModel>)

    @Delete
    fun delete(venues: DBVenuesModel)

    @Query("DELETE FROM venues")
    fun deleteAllVenues()

    @Transaction
    fun deleteAndSave(venues: List<DBVenuesModel>) {
        deleteAllVenues()
        insertAll(venues)
    }

    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): List<DBFavoriteModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addFavorite(vararg favorite: DBFavoriteModel)

    @Delete
    fun deleteFromFavorite(favorite: DBFavoriteModel)
}
