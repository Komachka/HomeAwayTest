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
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.base.BaseFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.android.synthetic.main.venues_list_fragment.*

class VenuesListFragment : BaseFragment(), VenuesListView {

    override fun updateItemView(venues: Venues) {
        log("update item view")
    }

    @Inject
    lateinit var presenter: VenuesListPresenter

    private lateinit var compositeDisposable: CompositeDisposable

    override fun setUp() {
        compositeDisposable = CompositeDisposable()

        (presenter as VenuesListPresenterImpl).attachView(this)
        fab.setOnClickListener { view ->
            presenter.navigateToMapScreen(view, queryEditText.text.toString())
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
                    presenter.addToFavorite(venue)
                }
            }
        }
        presenter.getFavorites()
        compositeDisposable.add(createTextChangeObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                presenter.showProgress()
                presenter.hideMupButton()
                if (it.isEmpty()) {
                    presenter.getFavorites()
                }
            }.filter { it.isNotEmpty() }
            .subscribeBy(
                onError = {
                    presenter.showError(it)
                },
                onNext = {
                    presenter.getVenues(it)
                })
        )
    }

    override fun destroy() {
        compositeDisposable.clear()
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
        return textChangeObservable
            .debounce(LOADING_TIMEOUT, TimeUnit.MILLISECONDS)
    }
}
