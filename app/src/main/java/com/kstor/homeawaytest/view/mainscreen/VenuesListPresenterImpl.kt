package com.kstor.homeawaytest.view.mainscreen

import androidx.navigation.NavController
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.view.base.AddAndRemoveFavoritesManager
import com.kstor.homeawaytest.view.base.BasePresenter
import com.kstor.homeawaytest.view.utils.DispatcherProvider
import com.kstor.homeawaytest.view.utils.VenuesMapper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VenuesListPresenterImpl @Inject constructor(
    private val getVenuesUseCase: VenuesUseCase,
    dispatcherProvider: DispatcherProvider,
    private val favoritesUseCase: FavoriteUseCase
) :
    VenuesListPresenter, AddAndRemoveFavoritesManager,
    BasePresenter<VenuesListView>(dispatcherProvider),
    VenuesMapper {

    override fun addAndRemoveFromFavorites(venue: Venue) {
        launch(Dispatchers.Default) {
            addAndRemoveFromFavorites(venue, favoritesUseCase)
        }
    }

    override fun getFavorites() {
        launch(Dispatchers.Default) {
            val result = favoritesUseCase.getFavorites()
            withContext(Dispatchers.Main) {
                when (result) {
                    is RepoResult.Success -> {
                        log("resutl ${result.data}")
                        view?.hideProgress()
                        view?.hideNoResult()
                        view?.displayVenues(result.data)
                    }
                    is RepoResult.Error<*> -> {
                        view?.displayVenues(emptyList())
                        view?.hideProgress()
                        view?.showError(result.throwable)
                        view?.showNoResult()
                    }
                }
            }
        }
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
        launch(Dispatchers.Default) {
            val repoResult = getVenuesUseCase.loadVenuesDataFromApi(query)
            withContext(Dispatchers.Main) {
                when (repoResult) {
                    is RepoResult.Success -> {
                        view?.hideProgress()
                        view?.hideNoResult()
                        view?.displayVenues(repoResult.data)
                        view?.showMupButn()
                    }
                    is RepoResult.Error<*> -> {
                        view?.hideProgress()
                        view?.showError(repoResult.throwable)
                        view?.displayVenues(emptyList())
                        view?.showNoResult()
                    }
                }
            }
        }
    }

    override fun navigateToDetailScreen(navController: NavController, venue: Venue) {
        mapToParcelize(venue)?.let {
            val action =
                VenuesListFragmentDirections.actionVenuesListFragmentToDetailFragment(it)
            navController.navigate(action)
        }
    }
}
