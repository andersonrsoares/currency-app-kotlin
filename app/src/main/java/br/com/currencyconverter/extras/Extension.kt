package br.com.currencyconverter.extras

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import br.com.currencyconverter.model.Currency
import org.threeten.bp.Instant
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, observer: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> observer(t) } })
}

fun List<Currency>.calcute(amount:Float):List<Currency>{
    this.forEach { it.calculated = it.value * amount }
    return this
}

fun Activity.hideKeyboard() {
    this.currentFocus?.also {view->
        view.hideKeyboard()
    }
}

fun View.hideKeyboard() {
    try {
        val inputManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Exception) {
        Log.e("hideKeyboard", e.toString(), e)
    }

}

fun Double?.format():String{
    val decimalFormat = DecimalFormat("#.####")
    decimalFormat.minimumFractionDigits = 2
    decimalFormat.maximumFractionDigits = 4

    return decimalFormat.format(this ?: "0.0")
}

fun Instant.format():String{
    return SimpleDateFormat("yyyy-MM-dd").format(Date(this@format.toEpochMilli()))
}

fun String.formatToView():String{
    return SimpleDateFormat("dd/MM/yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(this))
}
