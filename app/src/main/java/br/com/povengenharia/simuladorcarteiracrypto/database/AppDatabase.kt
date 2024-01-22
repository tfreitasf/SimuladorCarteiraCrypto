package br.com.povengenharia.simuladorcarteiracrypto.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.CryptoDao
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.CryptoFromApiDao
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.TransactionDao
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.WalletDao
import br.com.povengenharia.simuladorcarteiracrypto.model.Crypto
import br.com.povengenharia.simuladorcarteiracrypto.model.CryptoFromApi
import br.com.povengenharia.simuladorcarteiracrypto.model.Transaction
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
import br.com.povengenharia.simuladorcarteiracrypto.model.converter.BigDecimalConverter
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [Wallet::class, Transaction::class, Crypto::class, CryptoFromApi::class],
    version = 10,
    exportSchema = false
)
@TypeConverters(BigDecimalConverter::class)
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
                INSTANCE ?: buildDatabase(context, scope).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "wallet.db"
            )
                .addMigrations(MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10)
                .addCallback(AppDatabaseCallback(scope))
                .build()
            return database
        }
    }
}