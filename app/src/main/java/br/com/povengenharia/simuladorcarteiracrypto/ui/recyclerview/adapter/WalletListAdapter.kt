package br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.povengenharia.simuladorcarteiracrypto.databinding.WalletItemBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
import kotlinx.coroutines.CoroutineScope

class WalletListAdapter(
    private val context: Context,
    private val scope: CoroutineScope,
    wallets: List<Wallet> = emptyList(),
    var whenClickOnItem: (wallet: Wallet) -> Unit = {}
) : RecyclerView.Adapter<WalletListAdapter.ViewHolder>() {

    val wallets = wallets.toMutableList()

    inner class ViewHolder(private val binding: WalletItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var wallet: Wallet

        init {
            itemView.setOnClickListener {
                if (::wallet.isInitialized) {
                    whenClickOnItem(wallet)
                }
            }
        }

        fun bind(wallet: Wallet) {
            this.wallet = wallet
            binding.tvWalletItemWalletName.text = wallet.name
            binding.tvWalletItemWalletDescription.text = wallet.description
            binding.tvWalletItemWalletBallanceWallet.text =
                formatValueDollarCurrency(wallet.totalBalance.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WalletItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = wallets.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wallet = wallets[position]
        holder.bind(wallet)
    }

    fun update(wallets: List<Wallet>) {
        this.wallets.clear()
        this.wallets.addAll(wallets)
        notifyDataSetChanged()
    }

}
