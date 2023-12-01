package br.com.povengenharia.simuladorcarteiracrypto.database.webclient.model

data class CoinResponse(
    val status: String,
    val data: List<Coin>
)

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val price: String
)
