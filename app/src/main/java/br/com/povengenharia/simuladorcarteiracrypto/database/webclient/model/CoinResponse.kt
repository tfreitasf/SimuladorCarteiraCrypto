package br.com.povengenharia.simuladorcarteiracrypto.database.webclient.model

data class CoinResponse(
    val status: String,
    val data: CoinData
)

data class CoinData(
    val coins: List<Coin>
)

data class Coin(
    val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    val price: String,
    val change: String
)

