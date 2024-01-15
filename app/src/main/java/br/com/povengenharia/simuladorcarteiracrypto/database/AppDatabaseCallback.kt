package br.com.povengenharia.simuladorcarteiracrypto.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase.Companion.INSTANCE
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.WalletDao
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal


class AppDatabaseCallback(
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val database = INSTANCE ?: throw IllegalStateException("Database instance is null.")
        scope.launch {
            populateInitialData(database.walletDao())
        }
    }

    private suspend fun populateInitialData(walletDao: WalletDao) {
        val moneyWallet = Wallet(
            name = "Carteira de Dinheiro",
            description = "Carteira para dinheiro",
            type = "Money",
            totalBalance = BigDecimal.ZERO
        )
        walletDao.addWallet(moneyWallet)
    }
}
