package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityWalletDetailsBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.repository.CryptoWalletRepository
import br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter.CoinWalletAdapter
import kotlinx.coroutines.launch

class WalletDetailsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityWalletDetailsBinding.inflate(layoutInflater)
    }

    private val cryptoWalletRepository by lazy {
        val appDatabase = AppDatabase.getInstance(this, lifecycleScope)
        CryptoWalletRepository(appDatabase, lifecycleScope)
    }

    private val adapter by lazy {
        CoinWalletAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val walletID = intent.getIntExtra(KEY_WALLET_ID, -1)
        lifecycleScope.launch {
            configureBuyFab()
            setupRecyclerView()
            fetchCryptoForWallet(walletID)
        }
    }

    private fun setupRecyclerView() {
        binding.rvActivityWalletDetailsRecycleview.layoutManager = LinearLayoutManager(this)
        binding.rvActivityWalletDetailsRecycleview.adapter = adapter

        val walletID = intent.getIntExtra(KEY_WALLET_ID, -1)

        adapter.whenClickOnItem = {crypto ->
            val intent = Intent(this, CryptoDetailsActivity::class.java).apply {
                putExtra("EXTRA_COIN_UUID", crypto.uuid)
                putExtra(KEY_WALLET_ID, walletID)
            }
            startActivity(intent)

        }
    }

    private fun fetchCryptoForWallet(walletId: Int) {
        cryptoWalletRepository.fetchCryptoForWallet(walletId) { cryptos, totalWalletValue ->
            val cryptoInWallet = cryptos.filter {
                it.quantityOwned > 0.0 && it.price > 0.0
            }
            adapter.updateList(cryptoInWallet)
            updateTotalCryptoValue(totalWalletValue)
        }
    }

    private fun updateTotalCryptoValue(totalValue: Double) {
        binding.tvActivityWalletDetailsPropertyValues.text =
            formatValueDollarCurrency(totalValue.toString())
    }

    private fun configureBuyFab() {
        val fab = binding.fabActivityWalletDetailsBuyCrypto
        fab.setOnClickListener {
            cryptoListActivity()
        }
    }

    private fun cryptoListActivity() {
        val walletID = intent.getIntExtra(KEY_WALLET_ID, -1)
        val intent = Intent(this, CryptoListActivity::class.java).apply {
            putExtra(KEY_WALLET_ID, walletID)
        }
        startActivity(intent)
    }
}