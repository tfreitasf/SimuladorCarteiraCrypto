package br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.povengenharia.simuladorcarteiracrypto.databinding.CoinWalletItemBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.TryLoadImage
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.model.CoinWalletItem

class CoinWalletAdapter : RecyclerView.Adapter<CoinWalletAdapter.ViewHolder>() {

    private var items: List<CoinWalletItem> = listOf()

    class ViewHolder(private val binding: CoinWalletItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CoinWalletItem) {
            binding.ivCoinWalletItemSymbol.TryLoadImage(url = item.iconUrl) // Correção aqui
            binding.tvCoinWalletItemName.text = item.name
            binding.tvCoinWalletItemPrice.text = formatValueDollarCurrency(item.totalValue.toString())
            binding.tvCoinWalletItemSatoshiSymbol.text = item.symbol
            binding.tvCoinWalletItemSatoshi.text = item.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CoinWalletItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<CoinWalletItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}