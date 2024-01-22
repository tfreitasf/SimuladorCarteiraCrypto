package br.com.povengenharia.simuladorcarteiracrypto.repository

import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.model.Crypto
import br.com.povengenharia.simuladorcarteiracrypto.model.TransactionType
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CryptoWalletRepository(
    private val appDatabase: AppDatabase,
    private val scope: CoroutineScope
) {

    private val transactionDao = appDatabase.transactionDao()
    private val cryptoFromApiDao = appDatabase.cryptoFromApiDao()
    private val walletDao = appDatabase.walletDao()


    fun fetchCryptoForWallet(walletId: Int, onResult: (List<Crypto>, BigDecimal) -> Unit) {
        scope.launch {
            val transactions = transactionDao.getTransactionsForWalletByType(
                walletId,
                listOf(TransactionType.BUY, TransactionType.SELL)
            ).firstOrNull() ?: return@launch

            val cryptoQuantities = mutableMapOf<String, BigDecimal>()
            var totalWalletValue = BigDecimal.ZERO

            for (transaction in transactions) {
                val cryptoId = transaction.cryptoId ?: continue
                val currentAmount = cryptoQuantities.getOrDefault(cryptoId, BigDecimal.ZERO)
                val amountChange = transaction.quantity?.let {
                    if (transaction.type == TransactionType.BUY) it else it.negate()
                } ?: BigDecimal.ZERO
                cryptoQuantities[cryptoId] = currentAmount.add(amountChange)
            }

            val coinWalletItems = cryptoQuantities.mapNotNull { (cryptoId, quantity) ->
                val cryptoInfo =
                    cryptoFromApiDao.getCryptoById(cryptoId).firstOrNull() ?: return@mapNotNull null
                val totalValue = quantity.multiply(cryptoInfo.price)
                totalWalletValue = totalWalletValue.add(totalValue)
                Crypto(
                    uuid = cryptoInfo.uuid,
                    symbol = cryptoInfo.symbol,
                    name = cryptoInfo.name,
                    iconUrl = cryptoInfo.iconUrl,
                    price = cryptoInfo.price,
                    quantityOwned = quantity
                )
            }

            onResult(coinWalletItems, totalWalletValue)
        }
    }

    suspend fun calculateTotalWalletValue(walletId: Int): BigDecimal {
        val transactions = transactionDao.getTransactionsForWalletByType(
            walletId, listOf(TransactionType.BUY, TransactionType.SELL)
        ).firstOrNull() ?: return BigDecimal.ZERO

        val cryptoQuantities = mutableMapOf<String, BigDecimal>()
        transactions.forEach { transaction ->
            val currentAmount = cryptoQuantities.getOrDefault(
                transaction.cryptoId ?: return@forEach,
                BigDecimal.ZERO
            )
            val amountChange = transaction.quantity?.let {
                if (transaction.type == TransactionType.BUY) it else it.negate()
            } ?: BigDecimal.ZERO
            cryptoQuantities[transaction.cryptoId] = currentAmount.add(amountChange)
        }

        return cryptoQuantities.mapNotNull { (cryptoId, quantity) ->
            val cryptoInfo = cryptoFromApiDao.getCryptoById(cryptoId).firstOrNull() ?: return@mapNotNull null
            quantity.multiply(cryptoInfo.price)
        }.fold(BigDecimal.ZERO, BigDecimal::add)
    }

    suspend fun fetchQuantityOfCryptoInWallet(walletId: Int, cryptoUuid: String): BigDecimal {
        val transactions = transactionDao.getTransactionsForWalletByType(
            walletId, listOf(TransactionType.BUY, TransactionType.SELL)
        ).firstOrNull() ?: return BigDecimal.ZERO

        var totalQuantity = BigDecimal.ZERO
        transactions.filter { it.cryptoId == cryptoUuid }.forEach { transaction ->
            val amountChange = if (transaction.type == TransactionType.BUY)
                transaction.quantity ?: BigDecimal.ZERO
            else
                (transaction.quantity ?: BigDecimal.ZERO).negate()
            totalQuantity = totalQuantity.add(amountChange)
        }
        return totalQuantity
    }
    suspend fun getWalletById(walletId: Int): Wallet? {
        return walletDao.findById(walletId).firstOrNull()
    }
}



