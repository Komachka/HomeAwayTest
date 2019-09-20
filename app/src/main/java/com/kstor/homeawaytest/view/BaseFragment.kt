package com.kstor.homeawaytest.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), BaseView {

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
