package br.com.povengenharia.simuladorcarteiracrypto.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: TransactionType,
    val amount: Double,
    val walletId: Int,
    val cryptoId: Int? = null,
    val pricePerUnit: Double? = null,
    val quantity: Double? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
    DEPOSIT, WITHDRAW, BUY, SELL
}