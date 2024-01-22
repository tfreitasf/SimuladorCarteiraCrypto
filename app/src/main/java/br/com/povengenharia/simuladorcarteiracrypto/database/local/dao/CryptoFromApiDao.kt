package br.com.povengenharia.simuladorcarteiracrypto.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.povengenharia.simuladorcarteiracrypto.model.CryptoFromApi
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoFromApiDao {
    @Query("SELECT * FROM CryptoFromApi")
    fun getAllCryptos(): Flow<List<CryptoFromApi>>

    @Query("SELECT * FROM CryptoFromApi WHERE uuid = :uuid")
    fun getCryptoById(uuid: String): Flow<CryptoFromApi?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCrypto(crypto: CryptoFromApi)

    @Update
    suspend fun updateCrypto(crypto: CryptoFromApi)

    @Query("SELECT * FROM CryptoFromApi ORDER BY isFavorite DESC")
    fun getAllCryptosOrderedByFavorite(): Flow<List<CryptoFromApi>>

    @Query("SELECT * FROM CryptoFromApi WHERE name LIKE :searchQuery OR symbol LIKE :searchQuery")
    fun searchCryptos(searchQuery: String): Flow<List<CryptoFromApi>>
}