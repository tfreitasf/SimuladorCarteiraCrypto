package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.povengenharia.simuladorcarteiracrypto.R
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityCryptoDetailsBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.model.CryptoFromApi
import br.com.povengenharia.simuladorcarteiracrypto.repository.CryptoWalletRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.math.BigDecimal

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
    private val cryptoWalletRepository by lazy {
        val appDatabase = AppDatabase.getInstance(this, lifecycleScope)
        CryptoWalletRepository(appDatabase, lifecycleScope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureBuyButton()
        configureSellButton()

    }

    override fun onResume() {
        super.onResume()
        val cryptoUuid = intent.getStringExtra("EXTRA_COIN_UUID") ?: ""
        val walletId = intent.getIntExtra(KEY_WALLET_ID, -1)



        lifecycleScope.launch {
            fetchCryptoDetails(cryptoUuid)
            fetchMoneyWalletBalance()
            updateCryptoQuantityAndSymbol(cryptoUuid, walletId)
            cryptoFavorite(cryptoUuid)
        }
    }

    private suspend fun fetchCryptoDetails(cryptoUuid: String) {
        val crypto = cryptoFromApiDao.getCryptoById(cryptoUuid).firstOrNull()
        crypto?.let {
            binding.tvActivityCryptoDetailsLastPrice.text =
                formatValueDollarCurrency(it.price.toString())
            binding.tvActivityCryptoDetailsCryptoTitle.text = it.name
        }
    }

    private suspend fun cryptoFavorite(cryptoUuid: String) {
        val crypto = cryptoFromApiDao.getCryptoById(cryptoUuid).firstOrNull()
        if (crypto != null) {
            updateFavoriteIcon(crypto.isFavorite)
        }
        binding.ivactivityCryptoDetailsFavorite.setOnClickListener {
            if (crypto != null) {
                crypto.isFavorite = !crypto.isFavorite
                updateFavoriteIcon(crypto.isFavorite)
                updateFavoriteStatus(crypto)
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val favoriteIcon =
            if (isFavorite) R.drawable.ic_favorited else R.drawable.ic_favorite_border
        binding.ivactivityCryptoDetailsFavorite.setImageResource(favoriteIcon)
    }

    private fun updateFavoriteStatus(crypto: CryptoFromApi) {
        lifecycleScope.launch {
            cryptoFromApiDao.updateCrypto(crypto)
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

    private fun configureSellButton() {
        val button = binding.btnActivityCryptoDetailsSell
        button.setOnClickListener {
            sellForm()
        }
    }

    private fun sellForm() {
        val cryptoUuid = intent.getStringExtra("EXTRA_COIN_UUID") ?: ""
        val walletId = intent.getIntExtra(KEY_WALLET_ID, -1)

        lifecycleScope.launch {
            val quantity =
                cryptoWalletRepository.fetchQuantityOfCryptoInWallet(walletId, cryptoUuid)
            if (quantity > BigDecimal.ZERO) {
                val intent =
                    Intent(this@CryptoDetailsActivity, CryptoSellFormActivity::class.java).apply {
                        putExtra("EXTRA_COIN_UUID", cryptoUuid)
                        putExtra(KEY_WALLET_ID, walletId)
                    }
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@CryptoDetailsActivity,
                    "Você não possui esta criptomoeda nesta carteira",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun updateCryptoQuantityAndSymbol(cryptoUuid: String, walletId: Int) {
        val quantity = cryptoWalletRepository.fetchQuantityOfCryptoInWallet(walletId, cryptoUuid)
        val cryptoInfo = cryptoFromApiDao.getCryptoById(cryptoUuid).firstOrNull()

        binding.tvActivityCryptoDetailsAmountCryptoAvailable.text = String.format("%.8f", quantity)
        binding.tvActivityCryptoDetailsSymbolCryptoAvailable.text = cryptoInfo?.symbol
    }

}