package br.com.currencyconverter.di

import android.app.Application
import br.com.currencyconverter.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AppFragmentsModule::class,
        AppViewModelModule::class
    ]
)
interface AppComponent  {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)

}








