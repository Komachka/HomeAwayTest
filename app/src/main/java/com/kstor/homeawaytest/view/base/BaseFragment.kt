package com.kstor.homeawaytest.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kstor.homeawaytest.data.log

abstract class BaseFragment : Fragment(), BaseView {

    override fun showError(error: Throwable?) {
        error?.message?.let {
            log(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroy()
    }

    abstract fun setUp()
    abstract fun destroy()
}
