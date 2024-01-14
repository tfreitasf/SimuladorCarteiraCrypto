package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.povengenharia.simuladorcarteiracrypto.R
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.StarterRetrofit
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityMainBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
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
        binding.pbActivityMainProressbar.visibility = View.VISIBLE
        setContentView(binding.root)
        configureDepositFab()
        configureWithdrawFab()
        configureAddWalletFormFab()
    }

    override fun onResume() {
        super.onResume()

        if (!checkForInternet(this)) {
            updateUIBasedOnInternetAvailability()
            return
        }

        lifecycleScope.launch {
            coinRepository.fetchAndUpdateCryptoData()
            getAllWallet()
        }
    }

    private fun getAllWallet() {
        lifecycleScope.launch {
            try {

                walletDao.getAllWallet().collect { wallets ->
                    setupWalletList(wallets)
                    binding.pbActivityMainProressbar.visibility = View.GONE
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.txt_response_error),
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("MainActivity", "Error fetching wallets", e)
                binding.pbActivityMainProressbar.visibility = View.GONE
            }
        }
    }

    private suspend fun setupWalletList(wallets: List<Wallet>) {
        val cryptoWallets = wallets.filter { it.type == "Crypto" }
        adapter.update(cryptoWallets)
        updateMyProperty()
        cryptoWallets.forEach { wallet ->
            if (wallet.type == "Crypto") {
                cryptoWalletRepository.fetchCryptoForWallet(wallet.id) { _, totalWalletValue ->
                    adapter.updateWalletTotalBalance(wallet.id, totalWalletValue)
                }
            }
        }
        setupRecyclerView()
        binding.rvMainActivityRecyclerview.visibility = View.VISIBLE

        binding.pbActivityMainProressbar.visibility = View.GONE
        binding.ivActivityMainSignalNoData.visibility = View.GONE
        binding.tvActivityMainSignalNoData.visibility = View.GONE
    }


    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    private fun updateUIBasedOnInternetAvailability() {
        if (checkForInternet(this)) {
            binding.rvMainActivityRecyclerview.visibility = View.VISIBLE
            binding.pbActivityMainProressbar.visibility = View.GONE
            binding.ivActivityMainSignalNoData.visibility = View.GONE
            binding.tvActivityMainSignalNoData.visibility = View.GONE
        } else {
            binding.rvMainActivityRecyclerview.visibility = View.GONE
            binding.pbActivityMainProressbar.visibility = View.GONE
            binding.ivActivityMainSignalNoData.visibility = View.VISIBLE
            binding.tvActivityMainSignalNoData.visibility = View.VISIBLE
        }
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
            if (checkForInternet(this)) {
                depositForm()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.txt_no_internet_connection), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun depositForm() {
        val intent = Intent(this, DepositFormActivity::class.java)
        startActivity(intent)
    }

    private fun configureWithdrawFab() {
        val fab = binding.fabActivityMainWithdraw
        fab.setOnClickListener {
            if (checkForInternet(this)) {
                withdrawForm()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.txt_no_internet_connection), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun withdrawForm() {
        val intent = Intent(this, WithdrawActivityForm::class.java)
        startActivity(intent)
    }

    private fun configureAddWalletFormFab() {
        val fab = binding.efabActivityMainAddWallet
        fab.setOnClickListener {
            if (checkForInternet(this)) {
                addWalletForm()
            }else{
                Toast.makeText(
                    this,
                    getString(R.string.txt_no_internet_connection), Toast.LENGTH_SHORT
                ).show()
            }
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