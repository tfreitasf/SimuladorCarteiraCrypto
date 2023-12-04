package br.com.povengenharia.simuladorcarteiracrypto.database.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.povengenharia.simuladorcarteiracrypto.model.Transaction
import br.com.povengenharia.simuladorcarteiracrypto.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {


    @Query("SELECT * FROM `Transaction`")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` WHERE walletId = :walletId AND type IN (:types)")
    fun getTransactionsForWalletByType(
        walletId: Int,
        types: List<TransactionType>
    ): Flow<List<Transaction>>

    @Query("SELECT * FROM `Transaction` WHERE cryptoId = :cryptoId")
    fun getTransactionsForCrypto(cryptoId: Int): Flow<List<Transaction>>
}