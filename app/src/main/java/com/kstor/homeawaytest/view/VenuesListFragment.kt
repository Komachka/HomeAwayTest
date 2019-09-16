package com.kstor.homeawaytest.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.BASE_URL
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.network.VenuesRepositoryImp
import com.kstor.homeawaytest.data.network.VenuesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.venues_list_fragment.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        Toast.makeText(context, "list fragment ", Toast.LENGTH_LONG).show()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(VenuesService::class.java)
        val remoteData = RemoteData(service)
        val repo : VenuesRepositoryImp = VenuesRepositoryImp(remoteData)

        list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = VenuesListAdapter()
        }


        repo.getClosedVenuses(10, "coffe")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                log(it.toString())
            }
            .map {
                it.venues
            }
            .doOnError {
                log(it.toString())
            }
            .subscribe { venuesItem->
                log(venuesItem.toString())
                log("size ${venuesItem.size}")
                (list.adapter as VenuesListAdapter).updateData(venuesItem)
            }




    }


}
