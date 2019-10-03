package com.kstor.homeawaytest.view.customview

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.kstor.homeawaytest.R
import kotlinx.android.synthetic.main.view_error_snackbar.view.*

class CustomSnackBar(parent: ViewGroup, errorView: CustomSnackBarView) :
    BaseTransientBottomBar<CustomSnackBar>(parent, errorView, errorView) {

    init {

        getView().setBackgroundResource(R.drawable.snack_bar)
        getView().setPadding(0, 0, 0, 0)
        duration = LENGTH_LONG
    }

    companion object {
        fun make(view: View, mesage: String? = null): CustomSnackBar? {
            view.findSuitableParent()?.let { parent ->
                val customViewSnackbar = CustomSnackBarView(view.context)
                if (mesage != null) {
                    customViewSnackbar.message.text = mesage
                }
                return CustomSnackBar(
                    parent,
                    customViewSnackbar
                )
            }
            return null
        }
    }
}
fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                return view
            } else {
                fallback = view
            }
        }
        if (view != null) {
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)
    return fallback
}
