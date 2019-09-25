package com.kstor.homeawaytest.data.db

import com.kstor.homeawaytest.data.db.model.DBVenuesModel
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.data.mapToDBFavoriteModel
import io.reactivex.Observable
import io.reactivex.Single

class LocalData(private val venuesDao: VenuesDao) {
    fun getFavorites(): Single<List<DBVenuesModel>> {
        return Observable.fromCallable {
            venuesDao.getAllFavorites()
        }.map {
            it.map { DBVenuesModel(it.id, it.name, it.categoryId, it.categoryName, it.iconPath, it.address, it.distance, it.lat, it.lng, isFavorite = true) }
        }.firstOrError()
    }

    fun getAllVenues(): Single<List<DBVenuesModel>> {
        return venuesDao.getAll()
    }

    fun addToFavorites(venues: DBVenuesModel) {
        log(venues.toString())
        venuesDao.addFavorite(mapToDBFavoriteModel(venues))
    }

    fun saveToDB(venuesDBList: List<DBVenuesModel>) {
        val favoritesId = venuesDao.getAllFavorites().map { it.id }
        venuesDBList.filter { favoritesId.contains(it.id) }.map { it.isFavorite = true }
        venuesDao.insertAll(venuesDBList)
    }

    fun removeANdSaveVenues(venuesDBList: List<DBVenuesModel>) {
        val favoritesId = venuesDao.getAllFavorites().map { it.id }
        venuesDBList.filter { favoritesId.contains(it.id) }.map { it.isFavorite = true }
        venuesDao.deleteAndSave(venuesDBList)
    }

    fun removeFromFavorite(model: DBVenuesModel) {
        venuesDao.deleteFromFavorite(mapToDBFavoriteModel(model))
    }
}
