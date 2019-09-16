package com.kstor.homeawaytest.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.Venue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class VenuesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val venues = mutableListOf<Venue>()

    fun updateData(venues: List<Venue>) {
        this.venues.clear()
        this.venues.addAll(venues)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(venue: Venue) {
            view.venuesNameNameTextView.text = venue.name
            view.venuesCategory.text = venue.categories.joinToString { it.name }
            view.venuesNameAdressTextView.text = venue.address
            Picasso.get().load(venue.categories[0].iconPath).into(view.venuesPlaceImgView)
            view.venuesDistanceFromCenterTextView.text = "${venue.distance} m"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount() = venues.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(venues[position])
    }
}
