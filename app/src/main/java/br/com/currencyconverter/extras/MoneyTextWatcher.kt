package br.com.currencyconverter.extras

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class MoneyTextWatcher(private val editText: EditText) : TextWatcher {
    private var current = ""
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString() != current) {
            editText.removeTextChangedListener(this)

            try {
                val decimalFormat = DecimalFormat("#.##")
                decimalFormat.minimumFractionDigits = 2
                decimalFormat.maximumFractionDigits = 2
                val cleanString = s.toString().replace("[^\\d]".toRegex(), "")
                val parsed = java.lang.Double.parseDouble(cleanString)
                val formatted =
                    decimalFormat.format(parsed / 100)

                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)
            } catch (ex: Exception) {

            }

            editText.addTextChangedListener(this)
        }
    }

    override fun afterTextChanged(s: Editable) {

    }

    companion object {


        fun getCleanString(value: String): String {
            return value.replace("[^\\d]".toRegex(), "")
        }

        fun toFloat(value: String): Float {

            val cleanString = value.replace("[^\\d]".toRegex(), "")
            val parsed = java.lang.Double.parseDouble(cleanString)
            return (parsed / 100).toFloat()
        }

        fun getCleanCurrencyString(value: String): String {
            return value.replace("[^\\d]".toRegex(), "")
        }

        fun formatString(value: String): String {
            val decimalFormat = DecimalFormat("#.##")
            decimalFormat.minimumFractionDigits = 2
            decimalFormat.maximumFractionDigits = 2
            val format = decimalFormat.format(java.lang.Float.parseFloat(value).toDouble())

            val cleanString = format.toString().replace("[^\\d]".toRegex(), "")
            val parsed = java.lang.Double.parseDouble(cleanString)

            return decimalFormat.format(parsed / 100)
        }

        fun formatString(value: Float?): String {
            val decimalFormat = DecimalFormat("#.##")
            decimalFormat.minimumFractionDigits = 2
            decimalFormat.maximumFractionDigits = 2
            val format = decimalFormat.format(value)


            val cleanString = format.toString().replace("[^\\d]".toRegex(), "")
            val parsed = java.lang.Double.parseDouble(cleanString)

            return decimalFormat.format(parsed / 100)
        }
    }

}