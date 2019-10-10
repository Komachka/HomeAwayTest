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
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsPresenterImpl @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val useCase: GenerateStaticMapUrlUseCase,
    schedulerProvider: SchedulerProvider,
    private val favoritesUseCase: FavoriteUseCase,
    private val detailsUseCase: VenueDetailsUseCase

) : DetailsPresenter, AddAndRemoveFavoritesManager,
    BasePresenter<DetailsView>(compositeDisposable, schedulerProvider) {

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
        GlobalScope.launch(Dispatchers.Default) {
            addAndRemoveFromFavorites(venues, favoritesUseCase)
        }
    }

    override fun setFavorite(venues: Venue) {
        val favoriteLevel =
            if (venues.isFavorite) 1 else 0
        (view as DetailsView).setFavoriteDrawableLevel(favoriteLevel)
    }

    override fun createStaticMapUrl(venues: Venue) {
        compositeDisposable.add(useCase.createStaticMapUrl(venues)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onError = { view?.showError(it) },
                onComplete = {},
                onNext = {
                    (view as DetailsView).loadMap(it)
                }
            ))
    }

    override fun getVenueDetails(venue: Venue) {
        venue.id?.let {
            GlobalScope.launch(Dispatchers.Default) {
                val result = detailsUseCase.getVenueDetails(it)
                withContext(Dispatchers.Main) {
                    when (result) {
                        is RepoResult.Success -> {
                            (view as DetailsView).updateInfo(result.data)
                        }
                        is RepoResult.Error<*> -> {
                            view?.showError(result.throwable)
                        }
                    }
                }
            }
        }
    }
}
