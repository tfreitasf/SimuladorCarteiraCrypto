package br.com.povengenharia.simuladorcarteiracrypto.repository

import br.com.povengenharia.simuladorcarteiracrypto.BuildConfig
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.model.Coin
import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.service.CoinService
import br.com.povengenharia.simuladorcarteiracrypto.model.CryptoFromApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CoinRepository(
    private val coinService: CoinService,
    private val appDatabase: AppDatabase,
    private val scope: CoroutineScope
) {
    fun fetchAndUpdateCryptoData() {
        scope.launch {

            val response = coinService.getCoins(BuildConfig.COINRANKING_API_KEY)
            if (response.isSuccessful) {
                val coins = response.body()?.data?.coins ?: return@launch
                updateCryptoDataInDatabase(coins)
            }

        }
    }

    private suspend fun updateCryptoDataInDatabase(coins: List<Coin>) {
        val cryptoFromApiDao = appDatabase.cryptoFromApiDao()
        coins.forEach { coin ->
            val cryptoFromApi = CryptoFromApi(
                uuid = coin.uuid,
                symbol = coin.symbol,
                name = coin.name,
                iconUrl = coin.iconUrl,
                price = coin.price
            )
            cryptoFromApiDao.insertOrUpdateCrypto(cryptoFromApi)
        }
    }
}