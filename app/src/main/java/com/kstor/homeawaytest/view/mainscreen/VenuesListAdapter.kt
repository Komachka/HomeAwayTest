package com.kstor.homeawaytest.view.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.utils.FavoriteImageRes
import com.kstor.homeawaytest.view.utils.ImageLoader
import kotlinx.android.synthetic.main.list_item.view.*

class VenuesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ImageLoader {

    lateinit var detailsOnClickListener: (venues: Venues) -> Unit
    lateinit var addToFavoriteClickListener: (venues: Venues, pos: Int) -> Unit
    val venues = ArrayList<Venues>()

    fun updateData(venues: List<Venues>) {
        clearData()
        this.venues.addAll(venues)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        init {
            view.setOnClickListener(this)
            view.imageFavorite.setOnClickListener(this)
        }

        override fun onClick(item: View) {

            val position = adapterPosition
            if (item.id == R.id.imageFavorite) {
                val animation = AnimationUtils.loadAnimation(
                    view.context.applicationContext,
                    R.anim.zoomin
                )
                view.imageFavorite.startAnimation(animation)
                addToFavoriteClickListener.invoke(venues[position], position)
                venues[position].isFavorite = !venues[position].isFavorite
                notifyItemChanged(position)
            } else {
                detailsOnClickListener.invoke(venues[position])
            }
        }

        fun bind(venue: Venues) {
            view.venuesNameNameTextViewItem.text = venue.name
            view.venuesCategory.text =
                venue.categories?.name
            view.venuesNameAdressTextView.text = venue.address
            view.venuesDistanceFromCenterTextView.text = "${venue.distance} m"
            venue.categories?.let {
                it.iconPath.let {
                    view.venuesPlaceImgView.loadImage(it)
                }
            }
            setFavoriteDrawable(venue.isFavorite)
        }

        private fun setFavoriteDrawable(isFavorite: Boolean) {
            val imageRes = if (isFavorite) {
                FavoriteImageRes.IS_FAVORITE.resId
            } else {
                FavoriteImageRes.IS_NOT_FAVORITE.resId
            }
            view.imageFavorite.tag = imageRes
            view.imageFavorite.setImageResource(imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = venues.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(venues[position])
    }

    private fun clearData() {
        this.venues.clear()
    }

}
