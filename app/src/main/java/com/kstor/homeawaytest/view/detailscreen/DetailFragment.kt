package com.kstor.homeawaytest.view.detailscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kstor.homeawaytest.App
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.BaseFragment
import com.kstor.homeawaytest.view.ImageLoader
import com.kstor.homeawaytest.view.VenuesMapper
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import javax.inject.Inject
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : BaseFragment(), ImageLoader, DetailsView, VenuesMapper {


    override fun updateItemView(venues: Venues) {
        presenter.setFavorite(venues)
    }

    lateinit var presenter: DetailsPresenterImpl
    @Inject
    lateinit var useCase: GenerateStaticMapUrlUseCase

    @Inject
    lateinit var favoriteUseCase: FavoriteUseCase
    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).homeAwayComponents.inject(this)
    }

    override fun setUp() {
        presenter = DetailsPresenterImpl(useCase, schedulerProvider, favoriteUseCase )
        presenter.attachView(this)
        arguments?.let { bundle ->
            val venuesParselize = DetailFragmentArgs.fromBundle(bundle).venues
            mapToVenues(venuesParselize)?.let {venues ->
                presenter.createStaticMapUrl(venues)
                toolbar.apply {
                    title = venuesParselize.name
                }
                venuesNameNameTextView.text = venuesParselize.name
                venuesCategory.text =
                    venuesParselize.categories?.name
                venuesNameAdressTextView.text = venuesParselize.address
                venuesParselize.categories?.let {
                    it.iconPath?.let { path ->
                        venuesPlaceImgView.loadImage(path)
                    }
                }
                venuesDistanceFromCenterTextView.text = "${venuesParselize.distance} m"
                presenter.setFavorite(venues)
                fabFavorite.setOnClickListener {
                    venues.isFavorite = !venues.isFavorite
                    presenter.addAndRemoveFromFavorites(venues)
                }
            }

        }


    }

    override fun destroy() {
        presenter.detachView()
    }

    override fun loadMap(url: String?) {
        url?.let { path ->
            mapIv.loadImage(path)
        }
    }

    override fun setIfFavorite(resFavorites: Int) {
        fabFavorite.setImageResource(resFavorites)
    }
}
