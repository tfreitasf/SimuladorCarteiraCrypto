package br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.povengenharia.simuladorcarteiracrypto.R
import br.com.povengenharia.simuladorcarteiracrypto.databinding.CoinItemBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.TryLoadImage
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.model.CryptoFromApi
import br.com.povengenharia.simuladorcarteiracrypto.ui.activity.CryptoDetailsActivity
import br.com.povengenharia.simuladorcarteiracrypto.ui.activity.KEY_WALLET_ID

class CoinListAdapter(
    private var cryptos: List<CryptoFromApi>,
    private val context: Context,
    private val walletId: Int,
    private val onItemClicked: (CryptoFromApi) -> Unit,
    private val onFavoriteClicked: (CryptoFromApi) -> Unit
) : RecyclerView.Adapter<CoinListAdapter.ViewHolder>() {

    private fun setupFavorite(
        binding: CoinItemBinding,
        crypto: CryptoFromApi,

        ) {
        crypto.isFavorite = !crypto.isFavorite
        if (crypto.isFavorite)
            binding.ivCoinItemFavorite.setImageResource(R.drawable.ic_favorited)
        else
            binding.ivCoinItemFavorite.setImageResource(R.drawable.ic_favorite_border)

    }


    class ViewHolder(
        internal val binding: CoinItemBinding,
        private val context: Context,
        private val walletId: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crypto: CryptoFromApi) {
            binding.ivCoinItemSymbol.TryLoadImage(url = crypto.iconUrl)
            binding.tvCoinItemName.text = crypto.name
            binding.tvCoinItemPrice.text = formatValueDollarCurrency(crypto.price.toString())

            val favoriteIcon = if (crypto.isFavorite) {
                R.drawable.ic_favorited
            } else {
                R.drawable.ic_favorite_border
            }
            binding.ivCoinItemFavorite.setImageResource(favoriteIcon)

            binding.tvCoinItemChange.text = crypto.change
            val changeValue = crypto.change.toDoubleOrNull()
            if (changeValue != null && changeValue >= 0.0) { binding.tvCoinItemChange.setTextColor(Color.GREEN)} else {binding.tvCoinItemChange.setTextColor(Color.RED)}

            itemView.setOnClickListener {
                val intent = Intent(context, CryptoDetailsActivity::class.java).apply {
                    putExtra("EXTRA_COIN_UUID", crypto.uuid)
                    putExtra(KEY_WALLET_ID, walletId)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context, walletId)
    }

    override fun getItemCount(): Int = cryptos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crypto = cryptos[position]
        holder.bind(crypto)
        holder.binding.ivCoinItemFavorite.setOnClickListener {
            setupFavorite(holder.binding, crypto)
            onFavoriteClicked(crypto)
        }
    }

    fun updateList(newCryptos: List<CryptoFromApi>) {
        cryptos = newCryptos
        notifyDataSetChanged()
    }
}
