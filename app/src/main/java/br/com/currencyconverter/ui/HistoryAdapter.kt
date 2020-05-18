package br.com.currencyconverter.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import br.com.currencyconverter.R
import br.com.currencyconverter.extras.format
import br.com.currencyconverter.extras.formatToView
import br.com.currencyconverter.model.History
import java.util.concurrent.Executors


class HistoryAdapter : ListAdapter<History,HistoryAdapter.Holder>(
    AsyncDifferConfig.Builder<History>(diffCallback)
    .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
    .build()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.Holder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_history, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        binds(holder,getItem(position)!!,position)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    private fun binds(holder: Holder,data: History,position: Int) {
        with(holder){
            itemView.setOnClickListener {
                notifyItemChanged(position)
            }

            date.text = data.date.formatToView()
            valueA.text = data.calculatedA.format()
            valueB.text = data.calculatedB.format()
        }


    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val valueA: TextView = view.findViewById(R.id.valueA)
        val valueB: TextView = view.findViewById(R.id.valueB)
        val date: TextView = view.findViewById(R.id.date)
    }


    companion object {

        private val diffCallback: DiffUtil.ItemCallback<History> =
                object : DiffUtil.ItemCallback<History>() {

                    override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                        return oldItem == newItem
                    }
                }
    }
}

