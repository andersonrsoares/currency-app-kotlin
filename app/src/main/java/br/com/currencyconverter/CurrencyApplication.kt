package br.com.currencyconverter


import android.app.Application
import br.com.currencyconverter.di.AppComponent
import br.com.currencyconverter.di.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen


class CurrencyApplication : Application(){

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        init()
    }

    fun init(){
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}

fun Application.appComponent():AppComponent{
    return (this as CurrencyApplication).appComponent
}
