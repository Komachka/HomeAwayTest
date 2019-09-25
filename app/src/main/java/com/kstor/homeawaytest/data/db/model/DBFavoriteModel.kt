package com.kstor.homeawaytest.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class DBFavoriteModel(
    @PrimaryKey var id: String,
    val name: String,
    val categoryId: String,
    var categoryName: String,
    val iconPath: String,
    val address: String,
    val distance: Int,
    val lat: Double,
    val lng: Double
)
