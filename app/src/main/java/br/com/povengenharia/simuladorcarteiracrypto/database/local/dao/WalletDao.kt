package br.com.povengenharia.simuladorcarteiracrypto.database.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Query("SELECT * FROM Wallet")
    fun getAllWallet(): Flow<List<Wallet>>

    @Insert
    suspend fun addWallet(vararg wallet: Wallet)

    @Update
    suspend fun updateWallet(wallet: Wallet)

    @Delete
    suspend fun deleteWallet(wallet: Wallet)

    @Query("SELECT * FROM Wallet WHERE id = :id")
    fun findById(id: Int): Flow<List<Wallet?>>
}