package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.view.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<V>(private val dispatcherProvider: DispatcherProvider) :
    CoroutineScope {
    private var job = Job()
    override val coroutineContext: CoroutineContext = job + dispatcherProvider.io()
    var view: V? = null

    fun attachView(v: V) {
        view = v
    }

    fun detachView() {
        view = null
    }

    fun cancel() {
        job.cancel()
    }

    fun handleRepoResult(result: RepoResult<*>, success: () -> Unit, fail:() -> Unit)
    {
        when(result)
        {
            is RepoResult.Success -> success.invoke()
            is RepoResult.Error<*> -> fail.invoke()
        }
    }
}
