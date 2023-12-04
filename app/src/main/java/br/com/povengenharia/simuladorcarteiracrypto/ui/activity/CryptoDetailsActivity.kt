package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityCryptoDetailsBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CryptoDetailsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCryptoDetailsBinding.inflate(layoutInflater)
    }

    private val walletDao by lazy {
        val db = AppDatabase.getInstance(this, lifecycleScope)
        db.walletDao()
    }

    private val cryptoFromApiDao by lazy {
        AppDatabase.getInstance(this, lifecycleScope).cryptoFromApiDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureBuyButton()



    }

    override fun onResume() {
        super.onResume()
        val cryptoUuid = intent.getStringExtra("EXTRA_COIN_UUID") ?: ""
        val walletId = intent.getIntExtra(KEY_WALLET_ID, -1)
        Toast.makeText(
            this,
            "Carteira ID: $walletId, Crypto ID: $cryptoUuid",
            Toast.LENGTH_LONG
        ).show()

        lifecycleScope.launch {
            fetchCryptoDetails(cryptoUuid)
            fetchMoneyWalletBalance()
        }
    }

    private suspend fun fetchCryptoDetails(cryptoUuid: String) {
        val crypto = cryptoFromApiDao.getCryptoById(cryptoUuid).firstOrNull()
        crypto?.let {
            binding.tvActivityCryptoDetailsLastPrice.text =
                formatValueDollarCurrency(it.price)

        }
    }

    private suspend fun fetchMoneyWalletBalance() {
        val moneyWalletBalance = walletDao.findById(1).firstOrNull()?.totalBalance ?: 0.0
        binding.tvActivityCryptoDetailsAmountMoneyAvailable.text =
            formatValueDollarCurrency(moneyWalletBalance.toString())
    }

    private fun configureBuyButton() {
        val button = binding.btnActivityCryptoDetailsBuy
        button.setOnClickListener {
            buyForm()
        }
    }

    private fun buyForm() {
        val cryptoUuid = intent.getStringExtra("EXTRA_COIN_UUID") ?: ""
        val walletId = intent.getIntExtra(KEY_WALLET_ID, -1)

        val intent = Intent(this, CryptoBuyFormActivity::class.java).apply {
            putExtra("EXTRA_COIN_UUID", cryptoUuid)
            putExtra(KEY_WALLET_ID, walletId)
        }
        startActivity(intent)
    }

}