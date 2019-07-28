package com.tapp.safepof

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ActivityHistoryAdapter : RecyclerView.Adapter<ActivityHistoryAdapter.ViewHolder>() {
    var data = listOf<TransactionData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.balance.setText("$${String.format("%.2f", item.balance)}")
        val numFormatter = NumberFormat.getCurrencyInstance(Locale.US)
        val timeFormatter = SimpleDateFormat("MM/dd/yyyy")
        holder.transactionDate.setText(timeFormatter.format(item.transactionDate!!))
        if (item.transactionType == "Deposit") {
            holder.transactionTypeImage.setImageResource(R.drawable.add_button_orange)
            holder.transactionAmount.setText("${numFormatter.format(item.transactionAmount)}")
            holder.transactionTypeText.setText(R.string.deposit)
        }
        else {
            holder.transactionTypeImage.setImageResource(R.drawable.minus_button_blue)
            holder.transactionAmount.setText("-${numFormatter.format(item.transactionAmount)}")
            holder.transactionTypeText.setText(R.string.withdraw)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.activity_view_holder, parent, false)
        return ViewHolder(view)
    }

    // ViewHolder class for RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount_text)
        val balance: TextView = itemView.findViewById(R.id.balance_text)
        val transactionTypeImage: ImageView = itemView.findViewById(R.id.transaction_type_image)
        val transactionTypeText: TextView = itemView.findViewById(R.id.transaction_type_text)
        val transactionDate: TextView = itemView.findViewById(R.id.transaction_date)
    }
}