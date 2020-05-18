package br.com.currencyconverter.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.*
import br.com.currencyconverter.R
import br.com.currencyconverter.extras.format
import br.com.currencyconverter.model.Currency
import java.util.concurrent.Executors


class CurrencyAdapter : ListAdapter<Currency,CurrencyAdapter.Holder>(
    AsyncDifferConfig.Builder<Currency>(diffCallback)
    .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
    .build()){

    var selected = MutableLiveData<Boolean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyAdapter.Holder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_currency, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        binds(holder,getItem(position)!!,position)

    }

    private fun binds(holder: Holder,data: Currency,position: Int) {
        with(holder){
            itemView.setOnClickListener {
                currentList.filter { it.selected && it.name != data.name  }.also { list->
                    if(list.size >= 2) {
                        list.first().also {
                            it.selected = false
                            notifyItemChanged(currentList.indexOf(it))
                        }
                    }

                }
                data.selected = !data.selected
                selected.postValue(data.selected)
                notifyItemChanged(position)
            }

            if(data.selected){
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorPrimary))
                value.setTextColor(ContextCompat.getColor(itemView.context,android.R.color.white))
                currency.setTextColor(ContextCompat.getColor(itemView.context,android.R.color.white))
            }else{
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,android.R.color.white))
                value.setTextColor(ContextCompat.getColor(itemView.context,android.R.color.black))
                currency.setTextColor(ContextCompat.getColor(itemView.context,android.R.color.black))
            }

            value.text = data.calculated.format()
            currency.text = data.name
        }


    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val currency: TextView = view.findViewById(R.id.currency)
        val value: TextView = view.findViewById(R.id.value)
    }


    companion object {

        private val diffCallback: DiffUtil.ItemCallback<Currency> =
                object : DiffUtil.ItemCallback<Currency>() {

                    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                        return oldItem.name == newItem.name
                    }

                    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                        return oldItem == newItem
                    }
                }
    }
}

