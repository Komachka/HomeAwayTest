package com.kstor.homeawaytest.view.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kstor.homeawaytest.view.detailscreen.DetailViewModel

import com.kstor.homeawaytest.view.detailscreen.ViewModelFactory
import com.kstor.homeawaytest.view.detailscreen.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun detailViewModel(viewModel: DetailViewModel): ViewModel

    //Add more ViewModels here
}