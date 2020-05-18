package br.com.currencyconverter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import br.com.currencyconverter.R
import br.com.currencyconverter.appComponent
import br.com.currencyconverter.fragments.AppNavHostFragment
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {

    @Inject
    @Named("AppFragmentFactory")
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.appComponent().inject(this)
        setContentView(R.layout.activity_main)
        onRestoreInstanceState()
    }


    private fun onRestoreInstanceState(){
        val host = supportFragmentManager.findFragmentById(R.id.fragments_container)
        if(host == null){
            createNavHost()
        }
    }

    private fun createNavHost(){
        val navHost = AppNavHostFragment.create(R.navigation.app_nav_graph)
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragments_container,
                navHost
            )
            .setPrimaryNavigationFragment(navHost)
            .commit()
    }



}
