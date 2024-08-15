package com.lexmerciful.dpvmoney.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.data.model.BankAccount
import com.lexmerciful.dpvmoney.databinding.ItemAccountDetailBinding
import com.lexmerciful.dpvmoney.utils.Utils

class BankAccountAdapter() : RecyclerView.Adapter<BankAccountAdapter.MyViewHolder>() {

    lateinit var onItemClick: ((BankAccount) -> Unit)

    class MyViewHolder(val binding: ItemAccountDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(ItemAccountDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.itemView.context
        val bankAccount = differ.currentList[position]

        holder.binding.accountNameTextView.text = bankAccount.name
        holder.binding.accountNumberTextView.text = context.getString(R.string.account_no, bankAccount.number.toString())
        holder.binding.availableBalanceTextView.text = context.getString(R.string._available_balance, Utils.formatAsCurrency(bankAccount.balance))

        holder.itemView.setOnClickListener {
            onItemClick.invoke(bankAccount)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<BankAccount>() {
        override fun areItemsTheSame(
            oldItem: BankAccount,
            newItem: BankAccount
        ): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(
            oldItem: BankAccount,
            newItem: BankAccount
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
}