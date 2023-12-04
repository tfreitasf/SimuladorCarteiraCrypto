package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.StarterRetrofit
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityMainBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.repository.CoinRepository
import br.com.povengenharia.simuladorcarteiracrypto.repository.CryptoWalletRepository
import br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter.WalletListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val adapter by lazy {
        WalletListAdapter(this, scope = lifecycleScope)
    }

    private val walletDao by lazy {
        val db = AppDatabase.getInstance(this, lifecycleScope)
        db.walletDao()
    }
    private val cryptoWalletRepository by lazy {
        val appDatabase = AppDatabase.getInstance(this, lifecycleScope)
        CryptoWalletRepository(appDatabase, lifecycleScope)
    }


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val coinRepository: CoinRepository by lazy {
        val coinService = StarterRetrofit().coinService
        val appDatabase = AppDatabase.getInstance(this, CoroutineScope(Dispatchers.IO))
        CoinRepository(coinService, appDatabase, CoroutineScope(Dispatchers.IO))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureDepositFab()
        configureWithdrawFab()
        configureAddWalletFormFab()
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            walletDao.getAllWallet().collect { wallets ->
                adapter.update(wallets)
                updateMyProperty()
                wallets.forEach { wallet ->
                    if (wallet.type == "Crypto") {
                        cryptoWalletRepository.fetchCryptoForWallet(wallet.id) { _, totalWalletValue ->

                            adapter.updateWalletTotalBalance(wallet.id, totalWalletValue)
                        }
                    }
                }
                setupRecyclerView()
            }
        }
        coinRepository.fetchAndUpdateCryptoData()

    }

    private fun setupRecyclerView() {
        val recyclerView = binding.rvMainActivityRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.whenClickOnItem = {
            val intent = Intent(
                this,
                WalletDetailsActivity::class.java
            ).apply {
                putExtra(KEY_WALLET_ID, it.id)
            }
            startActivity(intent)

        }
    }

    private fun configureDepositFab() {
        val fab = binding.fabActivityMainDeposit
        fab.setOnClickListener {
            depositForm()
        }
    }

    private fun depositForm() {
        val intent = Intent(this, DepositFormActivity::class.java)
        startActivity(intent)
    }

    private fun configureWithdrawFab() {
        val fab = binding.fabActivityMainWithdraw
        fab.setOnClickListener {
            withdrawForm()
        }
    }

    private fun withdrawForm() {
        val intent = Intent(this, WithdrawActivityForm::class.java)
        startActivity(intent)
    }

    private fun configureAddWalletFormFab() {
        val fab = binding.efabActivityMainAddWallet
        fab.setOnClickListener {
            addWalletForm()
        }
    }

    private fun addWalletForm() {
        val intent = Intent(this, WalletFormActivity::class.java)
        startActivity(intent)
    }

    private suspend fun updateMyProperty() {
        val moneyWalletBalance = walletDao.findById(1).firstOrNull()?.totalBalance ?: 0.0

        val cryptoWalletsTotalBalance = walletDao.getAllWallet()
            .firstOrNull()
            ?.filter { it.type == "Crypto" }
            ?.map { wallet ->
                cryptoWalletRepository.calculateTotalWalletValue(wallet.id)
            }
            ?.sum() ?: 0.0

        val totalBalance = moneyWalletBalance + cryptoWalletsTotalBalance
        val formattedTotalBalance = formatValueDollarCurrency(totalBalance.toString())

        binding.tvActivityMainPropertyValues.text = formattedTotalBalance
    }


}