package com.kstor.homeawaytest.view.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.utils.ImageLoader
import kotlinx.android.synthetic.main.list_item.view.*

class VenuesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ImageLoader {

    lateinit var detailsOnClickListener: (venues: Venues) -> Unit
    lateinit var addToFavoriteClickListener: (venues: Venues) -> Unit
    private val venues = mutableListOf<Venues>()

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

            if (item.id == R.id.imageFavorite) {
                val animation = AnimationUtils.loadAnimation(
                    view.context.applicationContext,
                    R.anim.zoomin
                )
                view.imageFavorite.startAnimation(animation)
                addToFavoriteClickListener.invoke(venues[adapterPosition])
                venues[adapterPosition].isFavorite = !venues[adapterPosition].isFavorite
                notifyItemChanged(adapterPosition)
            } else
                detailsOnClickListener.invoke(venues[adapterPosition])
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
            if (isFavorite) {
                view.imageFavorite.tag = R.drawable.ic_favorite_black_24dp
                view.imageFavorite.setImageResource(R.drawable.ic_favorite_black_24dp) }
            else {
                view.imageFavorite.tag = R.drawable.ic_favorite_border_black_24dp
                view.imageFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
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
