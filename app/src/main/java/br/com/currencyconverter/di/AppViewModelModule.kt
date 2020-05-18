package br.com.currencyconverter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.currencyconverter.viewmodels.AppViewModelFactory
import br.com.currencyconverter.viewmodels.HistoryViewModel
import br.com.currencyconverter.viewmodels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class AppViewModelModule {

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Singleton
    @Binds
    @IntoMap
    @AppViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Singleton
    @Binds
    @IntoMap
    @AppViewModelKey(HistoryViewModel::class)
    abstract fun bindHistoryViewModel(viewModel: HistoryViewModel): ViewModel
}