package com.kstor.homeawaytest.data.db

import androidx.room.*
import com.kstor.homeawaytest.data.FAVORITE_TABLE_NAME
import com.kstor.homeawaytest.data.db.model.DBFavoriteModel

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM $FAVORITE_TABLE_NAME")
    suspend fun getAllFavorites(): List<DBFavoriteModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFavorite(vararg favorite: DBFavoriteModel)

    @Delete
    suspend fun deleteFromFavorite(favorite: DBFavoriteModel)
}
