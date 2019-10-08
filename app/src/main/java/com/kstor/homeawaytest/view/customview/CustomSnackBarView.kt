package com.kstor.homeawaytest.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.ContentViewCallback
import com.kstor.homeawaytest.R

class CustomSnackBarView : ConstraintLayout, ContentViewCallback {

    override fun animateContentOut(deley: Int, duration: Int) {
    }

    override fun animateContentIn(deley: Int, duration: Int) {
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet) : super(context, attr)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_error_snackbar, this)
        clipToPadding = false
    }
}
