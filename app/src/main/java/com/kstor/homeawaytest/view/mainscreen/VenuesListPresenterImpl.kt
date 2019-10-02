package com.kstor.homeawaytest.view.mainscreen

import androidx.navigation.NavController
import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.base.BasePresenter
import com.kstor.homeawaytest.view.base.BaseView
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import com.kstor.homeawaytest.view.utils.VenuesMapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class VenuesListPresenterImpl @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val getVenuesUseCase: VenuesUseCase,
    private val schedulerProvider: SchedulerProvider,
    private val favoritesUseCase: FavoriteUseCase
) :
    VenuesListPresenter, BasePresenter<VenuesListView>(compositeDisposable),
    VenuesMapper {

    override fun getFavorites() {
        compositeDisposable.add(favoritesUseCase.getFavorites().subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onSuccess = {
                    view?.hideProgress()
                    view?.displayVenues(it)
                }, onError = {
                    view?.hideProgress()
                    (view as BaseView).showError(it)
                }
            ))
    }

    override fun addAndRemoveFromFavorites(venues: Venues) { // TODO code duplicate
        val act = if (!venues.isFavorite) {
            { favoritesUseCase.addToFavorite(venues) }
        } else {
            { favoritesUseCase.removeFromFavorite(venues) }
        }
        compositeDisposable.add(
            act.invoke()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                    onComplete = {
                        view?.updateItemView(venues)
                    },
                    onError = {
                        view?.showError(it)
                    })
        )
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
        compositeDisposable.add(getVenuesUseCase.loadVenuesDataFromApi(query).subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onNext = {
                    view?.hideProgress()
                    view?.displayVenues(it)
                    if (it.isNotEmpty()) view?.showMupButn()
                }, onError = {
                    view?.hideProgress()
                    (view as BaseView).showError(it)
                }, onComplete = {
                    println("Complete")
                }
            ))
    }

    override fun navigateToDetailScreen(navController: NavController, venue: Venues) {
        mapToParcelize(venue)?.let {
            val action =
                VenuesListFragmentDirections.actionVenuesListFragmentToDetailFragment(it)
            navController.navigate(action)
        }
    }
}
