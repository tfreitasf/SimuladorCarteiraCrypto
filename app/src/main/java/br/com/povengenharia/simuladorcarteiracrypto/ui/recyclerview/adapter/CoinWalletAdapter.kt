package br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.povengenharia.simuladorcarteiracrypto.databinding.CoinWalletItemBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.TryLoadImage
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.model.Crypto

class CoinWalletAdapter(
    cryptoWallet: List<Crypto> = emptyList(),
    var whenClickOnItem: (crypto: Crypto) -> Unit = {}
) : RecyclerView.Adapter<CoinWalletAdapter.ViewHolder>() {

    private var cryptoWallet = cryptoWallet.toMutableList()

    inner class ViewHolder(private val binding: CoinWalletItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crypto: Crypto) {
            binding.ivCoinWalletItemSymbol.TryLoadImage(url = crypto.iconUrl)
            binding.tvCoinWalletItemName.text = crypto.name
            binding.tvCoinWalletItemPrice.text = formatValueDollarCurrency(crypto.price.toString())
            binding.tvCoinWalletItemSatoshiSymbol.text = crypto.symbol
            binding.tvCoinWalletItemSatoshi.text = crypto.quantityOwned.toString()

            itemView.setOnClickListener {
                whenClickOnItem(crypto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CoinWalletItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cryptoWallet[position])
    }

    override fun getItemCount(): Int = cryptoWallet.size

    fun updateList(newItems: List<Crypto>) {
        cryptoWallet = newItems.toMutableList()
        notifyDataSetChanged()
    }
}