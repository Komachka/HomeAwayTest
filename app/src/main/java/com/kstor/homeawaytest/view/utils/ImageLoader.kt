package com.kstor.homeawaytest.view.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso

interface ImageLoader {
    fun ImageView.loadImage(iconPath: String) {
        if (iconPath.isNotEmpty())
            Picasso.get().load(iconPath).into(this)
    }
}
