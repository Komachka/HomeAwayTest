package com.kstor.homeawaytest.view.detailscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kstor.homeawaytest.App
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.view.BaseFragment
import com.kstor.homeawaytest.view.ImageLoader
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : BaseFragment(), ImageLoader, DetailsView {

    lateinit var presenter: DetailsPresenterImpl
    @Inject
    lateinit var useCase: GenerateStaticMapUrlUseCase
    @Inject
    lateinit var schedulerProvider:SchedulerProvider

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
        presenter = DetailsPresenterImpl(useCase, schedulerProvider)
        presenter.attachView(this)
        arguments?.let { bundle ->
            val venues = DetailFragmentArgs.fromBundle(bundle).venues
            presenter.createStaticMapUrl(venues)
            toolbar.apply {
                title = venues.name
            }
            venuesNameNameTextView.text = venues.name
            venuesCategory.text =
                venues.categories?.let { category -> category.joinToString { it.name ?: "" } }
            venuesNameAdressTextView.text = venues.address
            venues.categories?.let {
                if (it.isNotEmpty()) {
                    it[0].iconPath?.let { path ->
                        venuesPlaceImgView.loadImage(path)
                    }
                }
            }
            venuesDistanceFromCenterTextView.text = "${venues.distance} m"
            presenter.setFavorite(venues)
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
