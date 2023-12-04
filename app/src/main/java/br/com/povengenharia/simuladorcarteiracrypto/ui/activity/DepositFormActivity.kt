package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.TransactionDao
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.WalletDao
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityDepositFormBinding
import br.com.povengenharia.simuladorcarteiracrypto.model.Transaction
import br.com.povengenharia.simuladorcarteiracrypto.model.TransactionType
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DepositFormActivity : AppCompatActivity() {


    private val transactionDao: TransactionDao by lazy {
        val db = AppDatabase.getInstance(this, lifecycleScope)
        db.transactionDao()
    }

    private val walletDao: WalletDao by lazy {
        val db = AppDatabase.getInstance(this, lifecycleScope)
        db.walletDao()
    }

    private val binding by lazy {
        ActivityDepositFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        saveDepositInWallet()
    }

    private fun saveDepositInWallet() {
        val btnsave = binding.btActivityDepositDeposit
        btnsave.setOnClickListener {
            val newDeposit = createNewDeposit()
            if(newDeposit != null){
                lifecycleScope.launch {
                    transactionDao.insertTransaction(newDeposit)
                    updateMoneyWalletBalance(newDeposit.amount)
                    finish()
                }
            }
        }
    }

    private fun createNewDeposit(): Transaction? {
        val amountStr = binding.etActivityDepositDepositAmount.text.toString()

        if (amountStr.isBlank()) {
            Toast.makeText(this, "Por favor, insira um valor.", Toast.LENGTH_SHORT).show()
            return null
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            Toast.makeText(this, "Valor inválido. Insira um número positivo.", Toast.LENGTH_SHORT)
                .show()
            return null
        }

        return Transaction(
            type = TransactionType.DEPOSIT,
            amount = amount,
            walletId = 1
        )
    }

    private suspend fun updateMoneyWalletBalance(depositAmount: Double) {
        val wallet = walletDao.findById(1).firstOrNull() ?: return
        val updatedWallet = wallet.copy(totalBalance = wallet.totalBalance + depositAmount)
        walletDao.updateWallet(updatedWallet)
    }


}
