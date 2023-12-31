package br.com.povengenharia.simuladorcarteiracrypto.repository

import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.model.Crypto
import br.com.povengenharia.simuladorcarteiracrypto.model.TransactionType
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CryptoWalletRepository(
    private val appDatabase: AppDatabase,
    private val scope: CoroutineScope
) {

    private val transactionDao = appDatabase.transactionDao()
    private val cryptoFromApiDao = appDatabase.cryptoFromApiDao()
    private val walletDao = appDatabase.walletDao()


    fun fetchCryptoForWallet(walletId: Int, onResult: (List<Crypto>, Double) -> Unit) {
        scope.launch {
            val transactions = transactionDao.getTransactionsForWalletByType(
                walletId,
                listOf(TransactionType.BUY, TransactionType.SELL)
            ).firstOrNull() ?: return@launch
            val cryptoQuantities = mutableMapOf<String, Double>()
            var totalWalletValue = 0.0

            for (transaction in transactions) {
                val currentAmount =
                    cryptoQuantities.getOrDefault(transaction.cryptoId ?: continue, 0.0)
                val amountChange = if (transaction.type == TransactionType.BUY) transaction.quantity
                    ?: 0.0 else -(transaction.quantity ?: 0.0)
                cryptoQuantities[transaction.cryptoId] = currentAmount + amountChange
            }

            val coinWalletItems = cryptoQuantities.mapNotNull { (cryptoId, quantity) ->
                val cryptoInfo =
                    cryptoFromApiDao.getCryptoById(cryptoId).firstOrNull() ?: return@mapNotNull null
                val totalValue = quantity * cryptoInfo.price.toDouble()
                totalWalletValue += totalValue
                Crypto(
                    uuid = cryptoInfo.uuid,
                    symbol = cryptoInfo.symbol,
                    name = cryptoInfo.name,
                    iconUrl = cryptoInfo.iconUrl,
                    price = cryptoInfo.price.toDouble(),
                    quantityOwned = quantity

                )
            }

            onResult(coinWalletItems, totalWalletValue)
        }
    }

    suspend fun calculateTotalWalletValue(walletId: Int): Double {
        val transactions = transactionDao.getTransactionsForWalletByType(
            walletId, listOf(TransactionType.BUY, TransactionType.SELL)
        ).firstOrNull() ?: return 0.0

        val cryptoQuantities = mutableMapOf<String, Double>()
        transactions.forEach { transaction ->
            val currentAmount =
                cryptoQuantities.getOrDefault(transaction.cryptoId ?: return@forEach, 0.0)
            val amountChange = if (transaction.type == TransactionType.BUY) transaction.quantity
                ?: 0.0 else -(transaction.quantity ?: 0.0)
            cryptoQuantities[transaction.cryptoId] = currentAmount + amountChange
        }

        return cryptoQuantities.mapNotNull { (cryptoId, quantity) ->
            val cryptoInfo =
                cryptoFromApiDao.getCryptoById(cryptoId).firstOrNull() ?: return@mapNotNull null
            quantity * cryptoInfo.price.toDouble()
        }.sum()
    }

    suspend fun fetchQuantityOfCryptoInWallet(walletId: Int, cryptoUuid: String): Double {
        val transactions = transactionDao.getTransactionsForWalletByType(
            walletId, listOf(TransactionType.BUY, TransactionType.SELL)
        ).firstOrNull() ?: return 0.0

        var totalQuantity = 0.0
        transactions.filter { it.cryptoId == cryptoUuid }.forEach { transaction ->
            val amountChange = if (transaction.type == TransactionType.BUY) transaction.quantity
                ?: 0.0 else -(transaction.quantity ?: 0.0)
            totalQuantity += amountChange
        }
        return totalQuantity
    }

    suspend fun getWalletById(walletId: Int): Wallet? {
        return walletDao.findById(walletId).firstOrNull()
    }


}



