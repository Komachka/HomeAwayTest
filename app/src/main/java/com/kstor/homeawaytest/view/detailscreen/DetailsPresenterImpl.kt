package com.kstor.homeawaytest.view.detailscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavController
import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.VenueDetailsUseCase
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.view.base.AddAndRemoveFavoritesManager
import com.kstor.homeawaytest.view.base.BasePresenter
import com.kstor.homeawaytest.view.utils.DispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.*

class DetailsPresenterImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val useCase: GenerateStaticMapUrlUseCase,
    private val favoritesUseCase: FavoriteUseCase,
    private val detailsUseCase: VenueDetailsUseCase

) : DetailsPresenter, AddAndRemoveFavoritesManager,
    BasePresenter<DetailsView>(dispatcherProvider) {

    override fun fillDetailsScreen(venues: Venue) {
        view?.fillDetailsScreen(venues)
    }

    override fun openBrowser(context: Context, url: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }

    override fun navigateBack(navController: NavController) {
        navController.popBackStack()
    }

    override fun addAndRemoveFromFavorites(venues: Venue) {
        launch(Dispatchers.Default) {
            addAndRemoveFromFavorites(venues, favoritesUseCase)
        }
    }

    override fun setFavorite(venues: Venue) {
        val favoriteLevel =
            if (venues.isFavorite) 1 else 0
        (view as DetailsView).setFavoriteDrawableLevel(favoriteLevel)
    }

    override fun createStaticMapUrl(venues: Venue) {
        launch {
            val url = useCase.createStaticMapUrl(venues)
            withContext(dispatcherProvider.ui()) {
                (view as DetailsView).loadMap(url)
            }
        }
    }

    override fun getVenueDetails(venue: Venue) {
        venue.id?.let {
            launch {
                val result = detailsUseCase.getVenueDetails(it)
                withContext(Dispatchers.Main) {
                    handleRepoResult(result,
                        success = {
                            (view as DetailsView).updateInfo((result as RepoResult.Success).data)
                        }, fail = {
                            view?.showError((result as RepoResult.Error<*>).throwable)
                        })
                }
            }
        }
    }
}
