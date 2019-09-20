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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kstor.homeawaytest.App
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.VenuesMapper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.android.synthetic.main.venues_list_fragment.*

class VenuesListFragment : Fragment(), VenuesMapper, VenuesListView {

    override fun displayVenues(results: List<Venues>) {
        (list.adapter as VenuesListAdapter).updateData(results)
    }

    private lateinit var preferencesManager: SharedPreferenceData

    /*@Inject
    lateinit var repo: VenuesRepository*/

    @Inject
    lateinit var presenter: VenuesListPresenterImpl

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.setView(this)
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
            .subscribe {
                presenter.getVenues(it)
            }
    }

    override fun showProgress() {
        progressBar.visibility = VISIBLE
    }

    override fun hideProgress() {
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
