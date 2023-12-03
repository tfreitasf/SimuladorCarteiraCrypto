package br.com.povengenharia.simuladorcarteiracrypto.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.WalletDao
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet

@Database(entities = [Wallet::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun walletDao(): WalletDao

    companion object{
        @Volatile
        private var db: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "wallet.db"
            ).build()
        }
    }


}