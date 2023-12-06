package br.com.povengenharia.simuladorcarteiracrypto.database.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.povengenharia.simuladorcarteiracrypto.model.Crypto
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {
    @Query("SELECT * FROM Crypto")
    fun getAllCryptos(): Flow<List<Crypto>>

    @Query("SELECT * FROM Crypto WHERE uuid = :uuid")
    fun getCryptoById(uuid: String): Flow<Crypto?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCrypto(crypto: Crypto)

    @Update
    suspend fun updateCrypto(crypto: Crypto)

    @Delete
    suspend fun deleteCrypto(crypto: Crypto)
}