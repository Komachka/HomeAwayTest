package com.kstor.homeawaytest.data.db

import com.kstor.homeawaytest.data.db.model.DBVenuesModel
import io.reactivex.Single

class LocalData(private val venuesDao: VenuesDao) {
    fun getFavorites(): Single<List<DBVenuesModel>> {
        return venuesDao.getAll()
    }

    fun addToFavorites(venues: DBVenuesModel) {
        venuesDao.insertAll(venues)
    }
}
