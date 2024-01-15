package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityCryptoSellFormBinding
import br.com.povengenharia.simuladorcarteiracrypto.model.Crypto
import br.com.povengenharia.simuladorcarteiracrypto.model.Transaction
import br.com.povengenharia.simuladorcarteiracrypto.model.TransactionType
import br.com.povengenharia.simuladorcarteiracrypto.repository.CryptoWalletRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class CryptoSellFormActivity : AppCompatActivity() {

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

    private val cryptoDao by lazy {
        AppDatabase.getInstance(this, lifecycleScope).cryptoDao()
    }

    private val transactionDao by lazy {
        AppDatabase.getInstance(this, lifecycleScope).transactionDao()
    }

    private val binding by lazy {
        ActivityCryptoSellFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val cryptoUuid = intent.getStringExtra("EXTRA_COIN_UUID") ?: ""
        val walletId = intent.getIntExtra(KEY_WALLET_ID, -1)

        binding.btActivitySellFormSell.setOnClickListener {
            val amountToSell = binding.etActivityCryptoSellFormDepositAmount.text.toString().toDoubleOrNull() ?: 0.0
            sellCrypto(walletId, cryptoUuid, amountToSell)
        }

        lifecycleScope.launch {
            fillQuantityField(walletId, cryptoUuid)
        }
    }

    private suspend fun fillQuantityField(walletId: Int, cryptoUuid: String) {
        val totalCryptoQuantity =
            cryptoWalletRepository.fetchQuantityOfCryptoInWallet(walletId, cryptoUuid)
        setupButtons(totalCryptoQuantity)
    }

    private fun setupButtons(totalCryptoQuantity: BigDecimal) {
        binding.btnActivitySellForm25.setOnClickListener {
            binding.etActivityCryptoSellFormDepositAmount.setText(
                (totalCryptoQuantity.multiply(BigDecimal("0.25"))).setScale(8, RoundingMode.HALF_UP).toPlainString()
            )
        }

        binding.btnActivitySellForm50.setOnClickListener {
            binding.etActivityCryptoSellFormDepositAmount.setText(
                (totalCryptoQuantity.multiply(BigDecimal("0.50"))).setScale(8, RoundingMode.HALF_UP).toPlainString()
            )
        }

        binding.btnActivitySellForm75.setOnClickListener {
            binding.etActivityCryptoSellFormDepositAmount.setText(
                (totalCryptoQuantity.multiply(BigDecimal("0.75"))).setScale(8, RoundingMode.HALF_UP).toPlainString()
            )
        }

        binding.btnActivitySellForm100.setOnClickListener {
            binding.etActivityCryptoSellFormDepositAmount.setText(
                totalCryptoQuantity.setScale(8, RoundingMode.HALF_UP).toPlainString()
            )
        }
    }

    private fun sellCrypto(cryptoWalletId: Int, cryptoUuid: String, amount: Double) {
        lifecycleScope.launch {
            val moneyWallet = walletDao.findById(1).firstOrNull()
            val cryptoWallet = walletDao.findById(cryptoWalletId).firstOrNull()
            val cryptoFromApi = cryptoFromApiDao.getCryptoById(cryptoUuid).firstOrNull()

            if (moneyWallet != null && cryptoWallet != null && cryptoFromApi != null) {
                val cryptoPrice = cryptoFromApi.price
                if (cryptoPrice <= BigDecimal.ZERO || BigDecimal(amount) <= BigDecimal.ZERO) {
                    Toast.makeText(
                        this@CryptoSellFormActivity,
                        "Dados inválidos",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val quantityToSell = BigDecimal(amount).setScale(8, RoundingMode.HALF_UP)

                val ownedCrypto = cryptoDao.getCryptoById(cryptoUuid).firstOrNull()
                val newQuantityOwned = (ownedCrypto?.quantityOwned ?: BigDecimal.ZERO) - quantityToSell.setScale(8, RoundingMode.HALF_UP)


                if (newQuantityOwned < BigDecimal.ZERO) {
                    Toast.makeText(
                        this@CryptoSellFormActivity,
                        "Quantidade insuficiente para vender",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val totalSaleAmount = quantityToSell.multiply(cryptoPrice)
                val newMoneyWalletBalance = moneyWallet.totalBalance.add(totalSaleAmount)
                walletDao.updateWallet(moneyWallet.copy(totalBalance = newMoneyWalletBalance))

                if (newQuantityOwned > BigDecimal.ZERO) {
                    cryptoDao.insertOrUpdateCrypto(
                        Crypto(
                            uuid = cryptoUuid,
                            symbol = cryptoFromApi.symbol,
                            name = cryptoFromApi.name,
                            iconUrl = cryptoFromApi.iconUrl,
                            price = cryptoPrice,
                            quantityOwned = newQuantityOwned
                        )
                    )
                } else {
                    ownedCrypto?.let { cryptoDao.deleteCrypto(it) }
                }

                transactionDao.insertTransaction(
                    Transaction(
                        type = TransactionType.SELL,
                        amount = totalSaleAmount,
                        walletId = cryptoWalletId,
                        cryptoId = cryptoUuid,
                        pricePerUnit = cryptoPrice,
                        quantity = quantityToSell
                    )
                )

                val updatedCryptoBalance = newQuantityOwned.multiply(cryptoPrice)
                walletDao.updateWallet(cryptoWallet.copy(totalBalance = updatedCryptoBalance))

                Toast.makeText(
                    this@CryptoSellFormActivity,
                    "Venda realizada com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this@CryptoSellFormActivity,
                    "Erro ao buscar dados da carteira ou da criptomoeda",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}