package com.kstor.homeawaytest.view.detailscreen

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.navigation.Navigation
import com.google.android.material.appbar.AppBarLayout

import com.kstor.homeawaytest.App
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.domain.model.VenueDetails
import com.kstor.homeawaytest.view.base.BaseFragment
import com.kstor.homeawaytest.view.utils.ImageLoader
import com.kstor.homeawaytest.view.utils.VenuesMapper
import javax.inject.Inject
import kotlin.math.abs
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.details.*

class DetailFragment : BaseFragment(), ImageLoader, DetailsView,
    VenuesMapper {

    override fun setFavoriteDrawableLevel(level: Int) {
        fabFavorite.setImageLevel(level)
    }

    @Inject
    lateinit var presenter: DetailsPresenterImpl
    private var wasCollapsed = false

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
        collapsingToolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        collapsingToolbarLayout.setExpandedTitleColor(resources.getColor(R.color.transparent))
        toolbar.apply {
            navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
            setNavigationOnClickListener {
                presenter.navigateBack(Navigation.findNavController(it))
            }
        }
        presenter.attachView(this)
        val animation = AnimationUtils.loadAnimation(
            context?.applicationContext,
            R.anim.rotate
        )
        load_more.startAnimation(animation)
        arguments?.let { bundle ->
            val venuesParselize = DetailFragmentArgs.fromBundle(bundle).venues
            mapToVenues(venuesParselize)?.let { venues ->
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
                    presenter.addAndRemoveFromFavorites(venues)
                    venues.isFavorite = !venues.isFavorite
                }
                appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBar, verticalOffset ->
                    if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0 && !wasCollapsed) {
                        //  Collapsed first time
                        wasCollapsed = true
                        presenter.getVenueDetails(venues)

                        setAdditionalFieldsVisible()
                        turnOffAnimation()
                    } else if (abs(verticalOffset) - appBarLayout.totalScrollRange != 0) {
                        // Expanded
                        setAdditionalFieldsInvisible()
                    } else {
                        //  Collapsed next time
                        setAdditionalFieldsVisible()
                    }
                })
            }
        }

    }

    private fun turnOffAnimation() {
        load_more.clearAnimation()
        load_more.visibility = View.INVISIBLE
    }

    private fun setAdditionalFieldsInvisible() {
        ratingBar.visibility = View.INVISIBLE
        raitingTv.visibility = View.INVISIBLE
        IsOpenTv.visibility = View.INVISIBLE
        bigPictureIV.visibility = View.INVISIBLE
        descriptionTv.visibility = View.INVISIBLE
        sceduleTV.visibility = View.INVISIBLE
        urlTv.visibility = View.INVISIBLE
        urlIv.visibility = View.INVISIBLE
        telephoneIv.visibility = View.INVISIBLE
        telephoneTv.visibility = View.INVISIBLE
    }

    private fun setAdditionalFieldsVisible() {
        ratingBar.visibility = View.VISIBLE
        raitingTv.visibility = View.VISIBLE
        IsOpenTv.visibility = View.VISIBLE
        bigPictureIV.visibility = View.VISIBLE
        descriptionTv.visibility = View.VISIBLE
        sceduleTV.visibility = View.VISIBLE
        urlTv.visibility = View.VISIBLE
        urlIv.visibility = View.VISIBLE
        telephoneIv.visibility = View.VISIBLE
        telephoneTv.visibility = View.VISIBLE
    }

    override fun updateItemView(venues: Venue) {
        presenter.setFavorite(venues)
    }

    override fun destroy() {
        presenter.detachView()
    }

    override fun loadMap(url: String?) {
        url?.let { path ->
            mapIv.loadImage(path)
        }
    }

    /*override fun setIfFavorite(resFavorites: Int) {
        context?.let {
            ImageViewCompat.setImageTintList(fabFavorite, ColorStateList.valueOf(ContextCompat.getColor(it, R.color.black)))
            fabFavorite.setImageResource(resFavorites)
            fabFavorite.tag = resFavorites
        }

    }*/

    override fun updateInfo(details: VenueDetails) {
        details.rating?.let {
            ratingBar.rating = (it * 0.5).toFloat()
            raitingTv.text = it.toString()
        }
        details.isOpen?.let {
            IsOpenTv.text = if (it) {
                resources.getString(R.string.open)
            } else {
                resources.getString(R.string.close)
            }
        }
        details.bestPhoto?.let { bigPictureIV.loadImage(it) }
        details.hoursPerDay?.let {
            sceduleTV.text = it.joinToString(separator = "\n") { it.days + " " + it.renderedTime }
        }
        details.description?.let { descriptionTv.text = it }
        details.url?.let { url ->
            urlTv.text = url
            urlTv.setOnClickListener { context?.let { presenter.openBrowser(it, url) } }
        }
        details.formattedPhone?.let { telephoneTv.text = it }
    }
}
