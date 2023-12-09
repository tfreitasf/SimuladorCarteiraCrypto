package br.com.povengenharia.simuladorcarteiracrypto.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class Wallet(
    @PrimaryKey (autoGenerate = true) val id: Int =0,
    val name: String,
    val description: String,
    val type: String,
    var totalBalance: Double
) : Parcelable
