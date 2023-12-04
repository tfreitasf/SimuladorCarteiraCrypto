package br.com.povengenharia.simuladorcarteiracrypto.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.WalletDao
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)


        AppDatabase.INSTANCE?.let { database ->
            scope.launch {
                populateInitialData(database.walletDao())
            }
        }
    }

    private suspend fun populateInitialData(walletDao: WalletDao) {
        val moneyWallet = Wallet(
            name = "Carteira de Dinheiro",
            description = "Carteira para dinheiro",
            type = "Money",
            totalBalance = 0.0
        )
        walletDao.addWallet(moneyWallet)
    }
}