package com.kstor.homeawaytest.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kstor.homeawaytest.data.VENUES_TABLE_NAME

@Entity(tableName = VENUES_TABLE_NAME)
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
    var isFavorite: Boolean
)
