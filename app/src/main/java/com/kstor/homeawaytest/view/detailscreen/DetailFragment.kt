package com.kstor.homeawaytest.view.detailscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.repos.StaticMapRepositoryImpl
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.view.ImageLoader
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment(), ImageLoader {

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let { context ->
            val repository = StaticMapRepositoryImpl(SharedPreferenceData(context))
            val useCase = GenerateStaticMapUrlUseCase(repository)
            viewModel = ViewModelProviders.of(this, DetailViewModelFactory(useCase))
                .get(DetailViewModel::class.java)
        }
        arguments?.let { bundle ->
            val venues = DetailFragmentArgs.fromBundle(bundle).venues
            viewModel.createStaticMapUrl(venues)
            viewModel.staticMapUrlLiveData.observe(this, Observer { path ->
                mapIv.loadImage(path)
            })
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
            fabFavorite.setIfFavorite(venues.isFavorite)
        }
    }
}

private fun FloatingActionButton.setIfFavorite(isFavorite: Boolean) {
    if (isFavorite)
        setImageResource(R.drawable.ic_favorite_black_24dp)
    else
        setImageResource(R.drawable.ic_favorite_border_black_24dp)
}
