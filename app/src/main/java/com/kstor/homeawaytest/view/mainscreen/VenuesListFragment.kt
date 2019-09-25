package com.kstor.homeawaytest.view.mainscreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kstor.homeawaytest.App
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.BaseFragment
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.android.synthetic.main.venues_list_fragment.*

class VenuesListFragment : BaseFragment(), VenuesListView {

    @Inject
    lateinit var useCases: VenuesUseCase

    @Inject
    lateinit var schedulerProvider: SchedulerProvider
    lateinit var presenter: VenuesListPresenter

    override fun setUp() {
        presenter =
            VenuesListPresenterImpl(useCases, schedulerProvider)
        (presenter as VenuesListPresenterImpl).attachView(this)

        fab.setOnClickListener { view ->
            presenter.navigateToMapScreen(view)
        }

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = VenuesListAdapter()
            (adapter as VenuesListAdapter).detailsOnClickListener = { venue ->
                view?.let {
                    presenter.navigateToDetailScreen(it, venue)
                }
            }
            (adapter as VenuesListAdapter).addToFavoriteClickListener = { venue ->
                view?.let {
                    log("add to favorite ${venue.name}")
                    presenter.addToFavorite(venue)
                }
            }
        }
        presenter.getFavorites()

        createTextChangeObservable().observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                presenter.showProgress()
                presenter.hideMupButton()
            }
            .subscribeBy(
                onError = {
                    presenter.showError(it)
                },
                onNext = {
                    (list.adapter as VenuesListAdapter).clearData()
                    presenter.getVenues(it)
                })
    }

    override fun destroy() {
        (presenter as VenuesListPresenterImpl).detachView()
    }

    override fun displayVenues(results: List<Venues>) {
        (list.adapter as VenuesListAdapter).updateData(results)
    }

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

    override fun showProgress() {
        progressBar.visibility = VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = GONE
    }

    override fun showMupButn() {
        fab.visibility = VISIBLE
    }

    override fun hideMupButn() {
        fab.visibility = INVISIBLE
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
