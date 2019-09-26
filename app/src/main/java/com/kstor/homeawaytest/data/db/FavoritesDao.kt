package com.kstor.homeawaytest.data.db

import androidx.room.*
import com.kstor.homeawaytest.data.db.model.DBFavoriteModel

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): List<DBFavoriteModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addFavorite(vararg favorite: DBFavoriteModel)

    @Delete
    fun deleteFromFavorite(favorite: DBFavoriteModel)
}