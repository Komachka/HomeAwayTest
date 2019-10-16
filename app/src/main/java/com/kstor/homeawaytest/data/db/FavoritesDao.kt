package com.kstor.homeawaytest.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kstor.homeawaytest.data.FAVORITE_TABLE_NAME
import com.kstor.homeawaytest.data.db.model.DBFavoriteModel

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM $FAVORITE_TABLE_NAME")
    fun getAllFavorites(): List<DBFavoriteModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addFavorite(vararg favorite: DBFavoriteModel)

    @Delete
    fun deleteFromFavorite(favorite: DBFavoriteModel)
}
