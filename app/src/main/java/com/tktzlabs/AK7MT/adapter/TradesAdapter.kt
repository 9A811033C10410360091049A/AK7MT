package com.tktzlabs.AK7MT.adapter

import SqliteDatabase
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tktzlabs.AK7MT.R
import com.tktzlabs.AK7MT.db.Trades
import java.util.*

internal class TradesAdapter(private val context: Context, listTrades: ArrayList<Trades>) : RecyclerView.Adapter<TradeViewHolder>(), Filterable {
    private var listTrades: ArrayList<Trades>
    private val mArrayList: ArrayList<Trades>
    private val mDatabase: SqliteDatabase

    init {
        this.listTrades = listTrades
        this.mArrayList = listTrades
        mDatabase = SqliteDatabase(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trade_list_layout, parent, false)
        return TradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TradeViewHolder, position: Int) {
        val trades = listTrades[position]
        holder.tvType.text = trades.type
        holder.tvAmount.text = trades.amount
        holder.tvPrice.text = trades.price
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                listTrades = if (charString.isEmpty()) {
                    mArrayList
                } else {
                    val filteredList = ArrayList<Trades>()
                    for (trades in mArrayList) {
                        if (trades.type.toLowerCase().contains(charString)) {
                            filteredList.add(trades)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = listTrades
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                listTrades =
                    filterResults.values as ArrayList<Trades>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return listTrades.size
    }

}