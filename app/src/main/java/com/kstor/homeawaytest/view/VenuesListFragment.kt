package com.kstor.homeawaytest.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.network.VenuesRepositoryImp
import com.kstor.homeawaytest.data.network.VenuesService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.venues_list_fragment.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class VenuesListFragment : Fragment() {

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

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(VenuesService::class.java)
        val remoteData = RemoteData(service)
        val repo = VenuesRepositoryImp(remoteData)

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = VenuesListAdapter()
        }


        createTextChangeObservable().observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                showProgress()
            }
            .observeOn(Schedulers.io())
            .flatMap {
                repo.getClosedVenuses(LOAD_LIMIT, it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.venues
            }.doOnError {
                log(it.toString())
            }
            .subscribe { venuesItem ->
                hideProgress()
                log(venuesItem.toString())
                (list.adapter as VenuesListAdapter).updateData(venuesItem)
            }

    }

    private fun showProgress() {
        progressBar.visibility = VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = GONE
    }


    private fun createTextChangeObservable(): Observable<String> {
        val textChangeObservable = Observable.create<String> { emitter ->

            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }
            }
            queryEditText.addTextChangedListener(textWatcher)
            emitter.setCancellable {
                queryEditText.removeTextChangedListener(textWatcher)
            }
        }
        return textChangeObservable.filter { it.length >= MIN_INPUT_LENGTH }.debounce(LOADING_TIMEOUT, TimeUnit.MILLISECONDS)
    }


}
