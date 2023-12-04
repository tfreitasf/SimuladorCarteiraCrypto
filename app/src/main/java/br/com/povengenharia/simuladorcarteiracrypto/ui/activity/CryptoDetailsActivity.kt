package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
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


        lifecycleScope.launch {

            fetchMoneyWalletBalance()
        }
    }

    override fun onResume() {
        super.onResume()
        val cryptoUuid = intent.getStringExtra("EXTRA_COIN_UUID") ?: ""
        lifecycleScope.launch {
            fetchCryptoDetails(cryptoUuid)

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

}