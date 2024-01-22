package br.com.povengenharia.simuladorcarteiracrypto.repository

import br.com.povengenharia.simuladorcarteiracrypto.BuildConfig
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.model.Coin
import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.service.CoinService
import br.com.povengenharia.simuladorcarteiracrypto.model.CryptoFromApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException
import java.math.BigDecimal

class CoinRepository(
    private val coinService: CoinService,
    private val appDatabase: AppDatabase,
    private val scope: CoroutineScope
) {
    suspend fun fetchAndUpdateCryptoData(): Result<Unit> {
        return try {
            val response = coinService.getCoins(BuildConfig.COINRANKING_API_KEY)
            if (response.isSuccessful) {
                val coins =
                    response.body()?.data?.coins
                coins?.let {
                    updateCryptoDataInDatabase(it)
                } ?: Result.failure(IOException("Failed to retrieve coins from API"))
            } else {
                Result.failure(IOException("API call unsuccessful with response code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(
                IOException(
                    "Exception occurred during API call: ${e.localizedMessage}",
                    e
                )
            )
        }
    }

    private suspend fun updateCryptoDataInDatabase(coins: List<Coin>): Result<Unit> {
        val cryptoFromApiDao = appDatabase.cryptoFromApiDao()
        return try {
            coins.forEach { coin ->
                val existingCrypto = cryptoFromApiDao.getCryptoById(coin.uuid).firstOrNull()
                val priceAsBigDecimal = try {
                    BigDecimal(coin.price)
                } catch (e: NumberFormatException) {
                    BigDecimal.ZERO
                }
                if (existingCrypto != null) {
                    val updatedCrypto  = existingCrypto.copy(
                        uuid = coin.uuid,
                        symbol = coin.symbol,
                        name = coin.name,
                        iconUrl = coin.iconUrl,
                        price = priceAsBigDecimal,
                        change = coin.change

                    )
                    cryptoFromApiDao.updateCrypto(updatedCrypto)
                } else {
                    val newCrypto = CryptoFromApi(
                        uuid = coin.uuid,
                        symbol = coin.symbol,
                        name = coin.name,
                        iconUrl = coin.iconUrl,
                        price = priceAsBigDecimal,
                        isFavorite = false,
                        change = coin.change
                    )
                    cryptoFromApiDao.insertOrUpdateCrypto(newCrypto)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(IOException("Failed to update database: ${e.localizedMessage}", e))
        }
    }
}