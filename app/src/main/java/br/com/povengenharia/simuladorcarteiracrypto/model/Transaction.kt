package br.com.povengenharia.simuladorcarteiracrypto.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: TransactionType,
    val amount: BigDecimal,
    val walletId: Int,
    val cryptoId: String? = null,
    val pricePerUnit: BigDecimal? = null,
    val quantity: BigDecimal? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
    DEPOSIT, WITHDRAW, BUY, SELL
}