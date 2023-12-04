package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityCryptoBuyFormBinding
import br.com.povengenharia.simuladorcarteiracrypto.model.Crypto
import br.com.povengenharia.simuladorcarteiracrypto.model.Transaction
import br.com.povengenharia.simuladorcarteiracrypto.model.TransactionType
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CryptoBuyFormActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCryptoBuyFormBinding.inflate(layoutInflater)
    }

    private val walletDao by lazy {
        AppDatabase.getInstance(this, lifecycleScope).walletDao()
    }

    private val cryptoFromApiDao by lazy {
        AppDatabase.getInstance(this, lifecycleScope).cryptoFromApiDao()
    }

    private val cryptoDao by lazy {
        AppDatabase.getInstance(this, lifecycleScope).cryptoDao()
    }

    private val transactionDao by lazy {
        AppDatabase.getInstance(this, lifecycleScope).transactionDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val cryptoUuid = intent.getStringExtra("EXTRA_COIN_UUID") ?: ""
        val walletId = intent.getIntExtra(KEY_WALLET_ID, -1)

        binding.btActivityDepositDeposit.setOnClickListener {
            val amountText = binding.etActivityCryptoBuyFormDepositAmount.text.toString()
            if (amountText.isNotEmpty()) {
                val amount = amountText.toDoubleOrNull()
                if (amount != null && amount > 0) {
                    buyCrypto(walletId, cryptoUuid, amount)
                } else {
                    Toast.makeText(this, "Digite um valor válido.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Digite um valor para comprar.", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun buyCrypto(cryptoWalletId: Int, cryptoUuid: String, amount: Double) {
        lifecycleScope.launch {
            val moneyWallet = walletDao.findById(1).firstOrNull()
            val cryptoWallet = walletDao.findById(cryptoWalletId).firstOrNull()
            val cryptoFromApi = cryptoFromApiDao.getCryptoById(cryptoUuid).firstOrNull()

            if (moneyWallet != null && cryptoWallet != null && cryptoFromApi != null) {
                val cryptoPrice = cryptoFromApi.price.toDouble()
                if (cryptoPrice <= 0) {
                    Toast.makeText(
                        this@CryptoBuyFormActivity,
                        "Preço da criptomoeda inválido",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val quantityToBuy = amount / cryptoPrice
                if (moneyWallet.totalBalance >= amount) {
                    val newMoneyWalletBalance = moneyWallet.totalBalance - amount
                    walletDao.updateWallet(moneyWallet.copy(totalBalance = newMoneyWalletBalance))

                    val ownedCrypto = cryptoDao.getCryptoById(cryptoUuid).firstOrNull()
                    val newQuantityOwned = (ownedCrypto?.quantityOwned ?: 0.0) + quantityToBuy
                    cryptoDao.insertOrUpdateCrypto(
                        Crypto(
                            cryptoUuid,
                            cryptoFromApi.symbol,
                            cryptoFromApi.name,
                            cryptoFromApi.iconUrl,
                            cryptoPrice,
                            newQuantityOwned
                        )
                    )

                    transactionDao.insertTransaction(
                        Transaction(
                            type = TransactionType.BUY,
                            amount = amount,
                            walletId = cryptoWalletId,
                            cryptoId = cryptoUuid,
                            pricePerUnit = cryptoPrice,
                            quantity = quantityToBuy
                        )
                    )

                    val updatedCryptoBalance = newQuantityOwned * cryptoPrice
                    walletDao.updateWallet(cryptoWallet.copy(totalBalance = updatedCryptoBalance))


                    Toast.makeText(
                        this@CryptoBuyFormActivity,
                        "Compra realizada com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@CryptoBuyFormActivity,
                        "Saldo insuficiente na carteira",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            } else {
                Toast.makeText(
                    this@CryptoBuyFormActivity,
                    "Erro ao buscar dados da carteira ou da criptomoeda",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }


}

