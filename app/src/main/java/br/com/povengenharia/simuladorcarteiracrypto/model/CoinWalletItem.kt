package br.com.povengenharia.simuladorcarteiracrypto.model

data class CoinWalletItem(
    val iconUrl: String,
    val name: String,
    val symbol: String,
    val quantity: Double,
    val totalValue: Double
)
