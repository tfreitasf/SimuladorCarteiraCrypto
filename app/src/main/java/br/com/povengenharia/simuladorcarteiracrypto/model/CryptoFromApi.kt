package br.com.povengenharia.simuladorcarteiracrypto.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class CryptoFromApi(
    @PrimaryKey val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    var price: BigDecimal,
    var isFavorite : Boolean = false,
    var change : String
)