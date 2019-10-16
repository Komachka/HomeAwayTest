package com.kstor.homeawaytest.view.mainscreen

import androidx.navigation.NavController
import com.kstor.homeawaytest.data.NO_FAVORITE_MESSAGE
import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.view.base.AddAndRemoveFavoritesManager
import com.kstor.homeawaytest.view.base.BasePresenter
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import com.kstor.homeawaytest.view.utils.VenuesMapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class VenuesListPresenterImpl @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val getVenuesUseCase: VenuesUseCase,
    schedulerProvider: SchedulerProvider,
    private val favoritesUseCase: FavoriteUseCase
) :
    VenuesListPresenter, AddAndRemoveFavoritesManager,
    BasePresenter<VenuesListView>(compositeDisposable, schedulerProvider),
    VenuesMapper {

    override fun addAndRemoveFromFavorites(venue: Venue) {
        addAndRemoveFromFavorites(venue, favoritesUseCase)
    }

    override fun getFavorites() {
        compositeDisposable.add(favoritesUseCase.getFavorites().subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSuccess {
                if (it.isEmpty()) {
                    throw Throwable(NO_FAVORITE_MESSAGE)
                }
            }
            .subscribeBy(
                onSuccess = {
                    view?.hideProgress()
                    view?.hideNoResult()
                    view?.displayVenues(it)
                }, onError = {
                    view?.displayVenues(emptyList())
                    view?.hideProgress()
                    view?.showError(it)
                    view?.showNoResult()
                }
            ))
    }

    override fun hideMupButton() {
        view?.hideMupButn()
    }

    override fun showError(throwable: Throwable) {
        view?.showError(throwable)
    }

    override fun showProgress() {
        view?.showProgress()
    }

    override fun navigateToMapScreen(navController: NavController, query: String) {
        navController
            .navigate(VenuesListFragmentDirections.actionVenuesListFragmentToMapFragment(query))
    }

    override fun getVenues(query: String) {
        compositeDisposable.add(getVenuesUseCase.loadVenuesDataFromApi(query).subscribeOn(
            schedulerProvider.io()
        )
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onNext = {
                    view?.hideProgress()
                    if (it.isEmpty()) {
                        view?.displayVenues(emptyList())
                        view?.showNoResult()
                    } else {
                        view?.hideNoResult()
                        view?.displayVenues(it)
                        view?.showMupButn()
                    }
                }, onError = {
                    view?.hideProgress()
                    view?.showError(it)
                    view?.displayVenues(emptyList())
                    view?.showNoResult()
                }
            ))
    }

    override fun navigateToDetailScreen(navController: NavController, venue: Venue) {
        mapToParcelize(venue)?.let {
            val action =
                VenuesListFragmentDirections.actionVenuesListFragmentToDetailFragment(it)
            navController.navigate(action)
        }
    }
}
