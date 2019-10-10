package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.db.LocalData
import com.kstor.homeawaytest.data.db.model.DBVenuesModel
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.Venue
import java.lang.Exception

class VenuesRepositoryImp(
    private val remoteData: RemoteData,
    private val preferenceData: SharedPreferenceData,
    private val localData: LocalData
) : VenuesRepository {

    override suspend fun getClosestVenusesCache(): RepoResult<List<Venue>> {
        getLocalData().let {data->
            return if (data.isNotEmpty()) RepoResult.Success(data)
            else RepoResult.Error<List<Venue>>(Throwable(EMPTY_VENUES_LIST_MESSAGE))
        }
    }

    override suspend fun getCityCenter(): RepoResult<Pair<Float, Float>> {
        preferenceData.getCityCenterInfo().let { result ->
            return if (preferenceData.isDataValid(result)) RepoResult.Success(result)
            else RepoResult.Error<List<Venue>>(Throwable(INVALID_CITY_CENTER))
        }
    }

    override suspend fun removeFromFavorite(venues: Venue): RepoResult<Boolean> {
        return try {
            mapToDBVenuesModel(venues)?.let {
                localData.removeFromFavorite(it)
            }
            RepoResult.Success(true)
        } catch (e: Exception) {
            RepoResult.Error<Boolean>(e)
        }
    }

    override suspend fun getFavorites(): RepoResult<List<Venue>> {
        mapToListOfVenues(localData.getFavorites()).let {data->
            return if (data.isNotEmpty()) RepoResult.Success(data)
            else RepoResult.Error<List<Venue>>(Throwable(EMPTY_VENUES_LIST_MESSAGE))
        }
    }

    override suspend fun saveToFavorite(venues: Venue): RepoResult<Boolean> {
        return try {
            mapToDBVenuesModel(venues)?.let {
                localData.addToFavorites(it)
            }
            RepoResult.Success(true)
        } catch (e: Exception) {
            RepoResult.Error<Boolean>(e)
        }
    }

    override suspend fun getClosestVenuses(limit: Int, query: String): RepoResult<List<Venue>> {
        getRemoteData(limit, query).let { remoteData ->
            if (remoteData is RepoResult.Success) {
                localData.removeANdSaveVenues(mapToDBVenuesModelList(remoteData.data))
            } else {
                return remoteData
            }
        }
        getLocalData().let { localData->
            return if (localData.isNotEmpty()) RepoResult.Success(localData)
            else RepoResult.Error<List<Venue>>(Throwable(LOCAL_DATA_EMPTY))
        }
    }

    private suspend fun mapToDBVenuesModelList(list: List<Venue>): List<DBVenuesModel> {
        val newList = mutableListOf<DBVenuesModel>()
        list.forEach {
            mapToDBVenuesModel(it)?.let { newModel -> newList.add(newModel) }
        }
        return newList
    }

    private suspend fun getLocalData(): List<Venue> {
        return mapToListOfVenues(localData.getAllVenues())
    }

    private suspend fun getRemoteData(limit: Int, query: String): RepoResult<List<Venue>> {
        remoteData.closedVenues(limit, query).let {result ->
            return when(result) {
                is ApiResult.Succsses -> {
                    val venuesData = result.data.mapToVenuesData()
                    preferenceData.setCityCenterInfo(
                        venuesData.citCenterlat,
                        venuesData.citCenterlng
                    )
                    RepoResult.Success(venuesData.venues)
                }
                is ApiResult.Error<*> -> RepoResult.Error<List<Venue>>(result.throwable)
            }
        }
    }
}
