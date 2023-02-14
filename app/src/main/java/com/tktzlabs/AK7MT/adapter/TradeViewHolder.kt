package com.tktzlabs.AK7MT.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tktzlabs.AK7MT.R

class TradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvType: TextView = itemView.findViewById(R.id.type)
    var tvAmount: TextView = itemView.findViewById(R.id.amount)
    var tvPrice: TextView = itemView.findViewById(R.id.price)
}