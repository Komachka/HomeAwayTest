package com.kstor.homeawaytest.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venues")
data class DBVenuesModel(
    @PrimaryKey var id: String,
    val name: String,
    val categoryId: String,
    var categoryName: String,
    val iconPath: String,
    val address: String,
    val distance: Int,
    val lat: Double,
    val lng: Double,
    val isFavorite: Boolean
)
