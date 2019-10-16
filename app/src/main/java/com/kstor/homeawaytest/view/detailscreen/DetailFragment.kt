package com.kstor.homeawaytest.view.detailscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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

    @Inject
    lateinit var presenter: DetailsPresenterImpl
    private var wasCollapsed = false

    private lateinit var setVisibleAnimation: Animation
    private lateinit var setInvisibleAnimation: Animation

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
        initAnimations()
        toolbar.apply {
            navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
            setNavigationOnClickListener {
                presenter.navigateBack(Navigation.findNavController(it))
            }
        }
        presenter.attachView(this)
        arguments?.let { bundle ->
            val venuesParselize = DetailFragmentArgs.fromBundle(bundle).venues
            mapToVenues(venuesParselize)?.let { venues ->
                presenter.fillDetailsScreen(venues)
            }
        }
    }

    override fun fillDetailsScreen(venues: Venue) {
        presenter.createStaticMapUrl(venues)
        toolbar.apply {
            title = venues.name
        }
        venuesNameNameTextView.text = venues.name
        venuesCategory.text =
            venues.categories?.name
        venuesNameAdressTextView.text = venues.address
        venues.categories?.let {
            it.iconPath?.let { path ->
                venuesPlaceImgView.loadImage(path)
            }
        }
        venuesDistanceFromCenterTextView.text = "${venues.distance} m"
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
                turnOffMoreAnimation()
            } else if (abs(verticalOffset) == 0) {
                // Expanded
                setAdditionalFieldsInvisible()
            } else if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0 && wasCollapsed) {
                //  Collapsed next time
                setAdditionalFieldsVisible()
            }
        })
    }

    private fun initAnimations() {
        setVisibleAnimation = AnimationUtils.loadAnimation(context?.applicationContext, R.anim.slide_up)
        setInvisibleAnimation = AnimationUtils.loadAnimation(context?.applicationContext, R.anim.slide_down)
        val loadindAnimation = AnimationUtils.loadAnimation(
            context?.applicationContext,
            R.anim.rotate
        )
        load_more.startAnimation(loadindAnimation)
    }

    private fun turnOffMoreAnimation() {
        load_more.clearAnimation()
        load_more.visibility = View.INVISIBLE
    }

    private fun setAdditionalFieldsInvisible() {
        ratingBar.setInvisible()
        raitingTv.setInvisible()
        IsOpenTv.setInvisible()
        bigPictureIV.setInvisible()
        descriptionTv.setInvisible()
        sceduleTV.setInvisible()
        urlTv.setInvisible()
        urlIv.setInvisible()
        telephoneIv.setInvisible()
        telephoneTv.setInvisible()
    }

    private fun setAdditionalFieldsVisible() {
        ratingBar.setVisible()
        raitingTv.setVisible()
        IsOpenTv.setVisible()
        bigPictureIV.setVisible()
        descriptionTv.setVisible()
        sceduleTV.setVisible()
        urlTv.setVisible()
        urlIv.setVisible()
        telephoneIv.setVisible()
        telephoneTv.setVisible()
    }

    override fun setFavoriteDrawableLevel(level: Int) {
        fabFavorite.setImageLevel(level)
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

    private fun View.setVisible() {
        if (visibility == View.INVISIBLE) {
            startAnimation(setVisibleAnimation)
            visibility = View.VISIBLE
        }
    }

    private fun View.setInvisible() {
        if (visibility == View.VISIBLE) {
            startAnimation(setInvisibleAnimation)
            visibility = View.INVISIBLE
        }
    }
}
