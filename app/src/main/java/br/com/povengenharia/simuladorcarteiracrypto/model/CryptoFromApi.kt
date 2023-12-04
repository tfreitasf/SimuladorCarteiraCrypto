package br.com.povengenharia.simuladorcarteiracrypto.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CryptoFromApi(
    @PrimaryKey val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    var price: String
)