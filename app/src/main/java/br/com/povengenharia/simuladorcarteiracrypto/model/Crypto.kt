package br.com.povengenharia.simuladorcarteiracrypto.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class Crypto (
    @PrimaryKey
    val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    val price: BigDecimal,
    val quantityOwned: BigDecimal = BigDecimal.ZERO

)