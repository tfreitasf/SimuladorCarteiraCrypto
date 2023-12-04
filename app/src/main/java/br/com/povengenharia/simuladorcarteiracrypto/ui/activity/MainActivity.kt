package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityMainBinding
import br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter.WalletListAdapter
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


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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
        setupRecyclerView()
        lifecycleScope.launch {
            walletDao.getAllWallet().collect { wallets ->
                Log.d("MainActivity", "Carteiras atualizadas: $wallets")
                adapter.update(wallets)
                updateMyProperty()
            }
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
        val intent = Intent(this, WithdrawActivity::class.java)
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
        lifecycleScope.launch {
            val moneyWalletBalance = walletDao.findById(1).firstOrNull()?.totalBalance ?: 0.0

            val cryptoWalletsTotalBalance = walletDao.getAllWallet()
                .firstOrNull()
                ?.filter { it.type == "Crypto" }
                ?.sumOf { it.totalBalance } ?: 0.0

            val totalBalance = moneyWalletBalance + cryptoWalletsTotalBalance

            val binding = binding.tvActivityMainPropertyValues
            binding.text =totalBalance.toString()
        }

    }
}