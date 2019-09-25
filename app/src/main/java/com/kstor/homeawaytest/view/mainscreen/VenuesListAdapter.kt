package com.kstor.homeawaytest.view.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.ImageLoader
import kotlinx.android.synthetic.main.list_item.view.*

class VenuesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ImageLoader {

    lateinit var detailsOnClickListener: (venue: Venues) -> Unit
    private val venues = mutableListOf<Venues>()

    fun updateData(venues: List<Venues>) {
        this.venues.clear()
        this.venues.addAll(venues)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(item: View) {
            detailsOnClickListener.invoke(venues[adapterPosition])
        }

        fun bind(venue: Venues) {
            view.venuesNameNameTextView.text = venue.name
            view.venuesCategory.text =
                venue.categories?.name
            view.venuesNameAdressTextView.text = venue.address
            view.venuesDistanceFromCenterTextView.text = "${venue.distance} m"
            venue.categories?.let {
                it.iconPath.let {
                        view.venuesPlaceImgView.loadImage(it)
                }
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
}
