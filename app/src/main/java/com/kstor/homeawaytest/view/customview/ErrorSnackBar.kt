package com.kstor.homeawaytest.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.kstor.homeawaytest.R
import kotlinx.android.synthetic.main.view_error_snackbar.view.*

class ErrorSnackBar(parent:ViewGroup, errorView:ErrorSnackBarView)
    : BaseTransientBottomBar<ErrorSnackBar>(parent, errorView, errorView) {

    init {

        getView().setBackgroundResource(R.drawable.snack_bar)
        getView().setPadding(0, 0, 0, 0)
        duration = LENGTH_INDEFINITE
    }

    companion object {
        fun make(view: View, mesage: String? = null): ErrorSnackBar? {
            view.findSuitableParent()?.let { parent ->
                val customViewSnackbar = ErrorSnackBarView(view.context)
                if (mesage != null) {
                    customViewSnackbar.message.text = mesage
                }
                return ErrorSnackBar(
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