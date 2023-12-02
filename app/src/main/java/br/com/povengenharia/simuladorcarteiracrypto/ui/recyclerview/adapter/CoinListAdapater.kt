package br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.model.Coin
import br.com.povengenharia.simuladorcarteiracrypto.databinding.CoinItemBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.TryLoadImage
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency

class CoinListAdapter(
    private var coins: List<Coin>
) : RecyclerView.Adapter<CoinListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: CoinItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: Coin) {
            binding.ivCoinItemSymbol.TryLoadImage(url = coin.iconUrl)
            binding.tvCoinItemName.text = coin.name
            binding.tvCoinItemPrice.text = formatValueDollarCurrency(coin.price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = coins.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coin = coins[position]
        holder.bind(coin)
    }

    fun updateList(newCoins: List<Coin>) {
        coins = newCoins
        notifyDataSetChanged()
    }
}
