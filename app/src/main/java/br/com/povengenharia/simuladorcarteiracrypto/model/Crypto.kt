package br.com.povengenharia.simuladorcarteiracrypto.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Crypto (
    @PrimaryKey
    val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    val price: Double,
    val quantityOwned: Double = 0.0

)