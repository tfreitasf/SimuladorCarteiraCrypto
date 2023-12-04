package br.com.povengenharia.simuladorcarteiracrypto.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.CryptoDao
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.CryptoFromApiDao
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.TransactionDao
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.WalletDao
import br.com.povengenharia.simuladorcarteiracrypto.model.Crypto
import br.com.povengenharia.simuladorcarteiracrypto.model.CryptoFromApi
import br.com.povengenharia.simuladorcarteiracrypto.model.Transaction
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Wallet::class, Transaction::class, Crypto::class, CryptoFromApi::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDao

    abstract fun transactionDao(): TransactionDao

    abstract fun cryptoDao(): CryptoDao

    abstract fun cryptoFromApiDao(): CryptoFromApiDao

    companion object {

        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wallet.db"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}