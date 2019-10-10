package com.kstor.homeawaytest.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kstor.homeawaytest.data.logError
import com.kstor.homeawaytest.view.customview.CustomSnackBar

abstract class BaseFragment : Fragment(), BaseView {

    override fun showError(error: Throwable?) {
        error?.message?.let { error ->
            logError(error)
            view?.let { CustomSnackBar.make(it, error)?.show() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()
    }

    abstract fun setUp()
    abstract fun destroy()
}
