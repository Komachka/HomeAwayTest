package com.kstor.homeawaytest.data.db

import com.kstor.homeawaytest.data.db.model.DBVenuesModel
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.data.mapToDBFavoriteModel

class LocalData(private val venuesDao: VenuesDao, private val favoritesDao: FavoritesDao) {
    suspend fun getFavorites(): List<DBVenuesModel> {
        return favoritesDao.getAllFavorites().map {
            DBVenuesModel(it.id, it.name, it.categoryId, it.categoryName, it.iconPath, it.address, it.distance, it.lat, it.lng, isFavorite = true)
        }
    }

    suspend fun getAllVenues(): List<DBVenuesModel> {
        return venuesDao.getAll()
    }

    suspend fun addToFavorites(venues: DBVenuesModel) {
        log("Add to favorite ${mapToDBFavoriteModel(venues).toString()}")
        favoritesDao.addFavorite(mapToDBFavoriteModel(venues))
    }

    suspend fun saveToDB(venuesDBList: List<DBVenuesModel>) {
        val favoritesId = favoritesDao.getAllFavorites().map { it.id }
        venuesDBList.filter { favoritesId.contains(it.id) }.map { it.isFavorite = true }
        venuesDao.insertAll(venuesDBList)
    }

    suspend fun removeANdSaveVenues(venuesDBList: List<DBVenuesModel>) {
        val favoritesId = favoritesDao.getAllFavorites().map { it.id }
        venuesDBList.filter { favoritesId.contains(it.id) }.map { it.isFavorite = true }
        venuesDao.deleteAndSave(venuesDBList)
    }

    suspend fun removeFromFavorite(model: DBVenuesModel) {
        favoritesDao.deleteFromFavorite(mapToDBFavoriteModel(model))
    }
}
