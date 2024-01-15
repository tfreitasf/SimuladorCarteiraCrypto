package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.povengenharia.simuladorcarteiracrypto.R
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityWalletDetailsBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.repository.CryptoWalletRepository
import br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter.CoinWalletAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

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

    private var walletId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarActivityWalletDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        walletId = intent.getIntExtra(KEY_WALLET_ID, -1)
        val walletID = intent.getIntExtra(KEY_WALLET_ID, -1)
        lifecycleScope.launch {
            val wallet = cryptoWalletRepository.getWalletById(walletId)
            wallet?.let {
                withContext(Dispatchers.Main) {
                    supportActionBar?.title = it.name
                }
            }
            configureBuyFab()
            setupRecyclerView()
            fetchCryptoForWallet(walletID)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_wallet_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_wallet_details_edit -> {
                lifecycleScope.launch {
                    val wallet = cryptoWalletRepository.getWalletById(walletId)
                    wallet?.let {
                        val editIntent = Intent(
                            this@WalletDetailsActivity,
                            WalletFormActivity::class.java
                        ).apply {
                            putExtra(KEY_WALLET_ID, it)
                        }
                        startActivity(editIntent)
                    }
                }
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        binding.rvActivityWalletDetailsRecycleview.layoutManager = LinearLayoutManager(this)
        binding.rvActivityWalletDetailsRecycleview.adapter = adapter

        val walletID = intent.getIntExtra(KEY_WALLET_ID, -1)

        adapter.whenClickOnItem = { crypto ->
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
                it.quantityOwned.compareTo(BigDecimal.ZERO) > 0 && it.price.compareTo(BigDecimal.ZERO) > 0
            }
            adapter.updateList(cryptoInWallet)
            updateTotalCryptoValue(totalWalletValue.toDouble())
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