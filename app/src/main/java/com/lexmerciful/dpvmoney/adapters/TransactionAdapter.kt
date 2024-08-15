package com.lexmerciful.dpvmoney.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.data.model.Transaction
import com.lexmerciful.dpvmoney.databinding.ItemTransactionBinding
import com.lexmerciful.dpvmoney.utils.Utils

class TransactionAdapter() : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    var showTransactionDetails = false
    lateinit var userBankAccount: BankAccount

    class MyViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.itemView.context
        val transaction = differ.currentList[position]

        holder.binding.destinationNameTextView.text = transaction.destinationAccount.name
        holder.binding.paymentTypeSourceNameTextView.text = context.getString(R.string._payment_type_source, transaction.sourceAccount.name)
        holder.binding.destinationAccNoTextView.text = transaction.destinationAccount.number.toString()

        if (showTransactionDetails) {
            if (userBankAccount.number == transaction.sourceAccount.number) {
                holder.binding.transferAmountTextView.text = "- ${Utils.formatAsCurrency(transaction.amount)}"
                holder.binding.transferAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.dim_gray))
            } else {
                holder.binding.transferAmountTextView.text = "+ ${Utils.formatAsCurrency(transaction.amount)}"
                holder.binding.transferAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.light_green))
            }
        } else {
            holder.binding.transferAmountTextView.text = Utils.formatAsCurrency(transaction.amount)
        }

        holder.binding.dateTextView.text = Utils.getReformattedDate(transaction.transactionDate)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(
            oldItem: Transaction,
            newItem: Transaction
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Transaction,
            newItem: Transaction
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
}