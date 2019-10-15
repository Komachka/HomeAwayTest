package com.kstor.homeawaytest.view.utils

import com.kstor.homeawaytest.R

enum class FavoriteImageRes(val resId: Int) {
    IS_FAVORITE(R.drawable.ic_favorite_black_24dp),
    IS_NOT_FAVORITE(R.drawable.ic_favorite_border_black_24dp)
}

enum class FavoriteImageLevel(val level: Int) {
    IS_FAVORITE(1),
    IS_NOT_FAVORITE(0)
}
