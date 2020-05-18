package br.com.currencyconverter.di

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import br.com.currencyconverter.fragments.AppFragmentFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
object AppFragmentsModule {

    @JvmStatic
    @Singleton
    @Provides
    @Named("AppFragmentFactory")
    fun provideMainFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return AppFragmentFactory(
            viewModelFactory
        )
    }


}