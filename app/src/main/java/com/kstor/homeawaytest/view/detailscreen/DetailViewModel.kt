package com.kstor.homeawaytest.view.detailscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailViewModel(private val useCase: GenerateStaticMapUrlUseCase) : ViewModel() {

    val staticMapUrlLiveData = MutableLiveData<String>()

    fun createStaticMapUrl(venues: VenuesParcelize) {
        useCase.createStaticMapUrl(venues)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                staticMapUrlLiveData.postValue(it)
            }
    }
}

class DetailViewModelFactory(private val useCase: GenerateStaticMapUrlUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            DetailViewModel(this.useCase) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
