package br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.povengenharia.simuladorcarteiracrypto.databinding.CoinItemBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.TryLoadImage
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.model.CryptoFromApi
import br.com.povengenharia.simuladorcarteiracrypto.ui.activity.CryptoDetailsActivity

class CoinListAdapter(
    private var cryptos: List<CryptoFromApi>,
    private val context: Context
) : RecyclerView.Adapter<CoinListAdapter.ViewHolder>() {


    class ViewHolder(
        private val binding: CoinItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crypto: CryptoFromApi) {
            binding.ivCoinItemSymbol.TryLoadImage(url = crypto.iconUrl)
            binding.tvCoinItemName.text = crypto.name
            binding.tvCoinItemPrice.text = formatValueDollarCurrency(crypto.price)

            itemView.setOnClickListener {
                val intent = Intent(context, CryptoDetailsActivity::class.java).apply {
                    putExtra("EXTRA_COIN_UUID", crypto.uuid)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun getItemCount(): Int = cryptos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crypto = cryptos[position]
        holder.bind(crypto)
    }

    fun updateList(newCryptos: List<CryptoFromApi>) {
        cryptos = newCryptos
        notifyDataSetChanged()
    }
}
