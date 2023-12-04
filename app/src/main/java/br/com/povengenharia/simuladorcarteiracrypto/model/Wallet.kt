package br.com.povengenharia.simuladorcarteiracrypto.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Wallet(
    @PrimaryKey (autoGenerate = true) val id: Int =0,
    val name: String,
    val description: String,
    val type: String,
    var totalBalance: Double
)
