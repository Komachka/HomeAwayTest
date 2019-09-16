package com.kstor.homeawaytest.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

import com.kstor.homeawaytest.R
import kotlinx.android.synthetic.main.venues_list_fragment.*

class VenuesListFragment : Fragment() {

    companion object {
        fun newInstance() = VenuesListFragment()
    }

    private lateinit var viewModel: VenuesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.venues_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VenuesListViewModel::class.java)

        // TODO: Use the ViewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        Toast.makeText(context, "list fragment ", Toast.LENGTH_LONG).show()
    }
}
