package com.kstor.homeawaytest.view.mainscreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kstor.homeawaytest.App
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.VenusData
import com.kstor.homeawaytest.view.VenuesMapper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.android.synthetic.main.venues_list_fragment.*

class VenuesListFragment : Fragment(), VenuesMapper {

    private lateinit var viewModel: VenuesListViewModel
    private lateinit var preferencesManager: SharedPreferenceData

    @Inject lateinit var repo: VenuesRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.venues_list_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).homeAwayComponents.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(VenuesListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        context?.let {
            preferencesManager = SharedPreferenceData(it.applicationContext)
        }

        fab.setOnClickListener { view ->
        }

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = VenuesListAdapter()
            (adapter as VenuesListAdapter).detailsOnClickListener = { venue ->
                map(venue)?.let {
                    val action =
                        VenuesListFragmentDirections.actionVenuesListFragmentToDetailFragment(it)
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }

        createTextChangeObservable().observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                showProgress()
            }
            .observeOn(Schedulers.io())
            .flatMap {
                repo.getClosedVenuses(LOAD_LIMIT, it)
            }
            .doOnNext {
                saveCityCenterData(it)
            }
            .map {
                it.venues
            }.doOnError {
                log(it.toString())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { venuesItem ->
                hideProgress()
                log(venuesItem.toString())
                (list.adapter as VenuesListAdapter).updateData(venuesItem)
            }
    }

    private fun saveCityCenterData(venusData: VenusData?) {
        if (::preferencesManager.isInitialized) {
            venusData?.let {
                preferencesManager.setCityCenterInfo(it.citCenterlat, it.citCenterlng)
            }
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
                queryEditText?.removeTextChangedListener(textWatcher)
            }
        }
        return textChangeObservable.filter { it.length >= MIN_INPUT_LENGTH }
            .debounce(LOADING_TIMEOUT, TimeUnit.MILLISECONDS)
    }
}
