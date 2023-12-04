package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.TransactionDao
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.WalletDao
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityWithdrawFormBinding
import br.com.povengenharia.simuladorcarteiracrypto.extensions.formatValueDollarCurrency
import br.com.povengenharia.simuladorcarteiracrypto.model.Transaction
import br.com.povengenharia.simuladorcarteiracrypto.model.TransactionType
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class WithdrawActivityForm : AppCompatActivity() {

    private val transactionDao: TransactionDao by lazy {
        val db = AppDatabase.getInstance(this, lifecycleScope)
        db.transactionDao()
    }

    private val walletDao: WalletDao by lazy {
        val db = AppDatabase.getInstance(this, lifecycleScope)
        db.walletDao()
    }

    private val binding by lazy {
        ActivityWithdrawFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            loadMoneyWalletBalance()
        }
        setupWithdrawButton()

    }

    private fun setupWithdrawButton() {
        val btnsave = binding.btActivityWithdrawWithdraw
        btnsave.setOnClickListener {
            performWithdraw()
        }
    }

    private fun performWithdraw() {
        val amountStr = binding.etActivityWithdrawWithdrawAmount.text.toString()
        val amount = validateAmount(amountStr) ?: return

        lifecycleScope.launch {
            val moneyWalletBalance = walletDao.findById(1).firstOrNull()?.totalBalance ?: 0.0
            val formattedBalance = formatValueDollarCurrency(moneyWalletBalance.toString())
            if (amount > moneyWalletBalance) {
                Toast.makeText(this@WithdrawActivityForm, "Saldo insuficiente. Insira um valor menor ou igual ao seu saldo $formattedBalance.", Toast.LENGTH_LONG).show()
                return@launch
            }

            val newWithdraw = Transaction(
                type = TransactionType.WITHDRAW,
                amount = amount,
                walletId = 1
            )
            transactionDao.insertTransaction(newWithdraw)
            updateMoneyWalletBalance(amount)
            finish()
        }
    }

    private fun validateAmount(amountStr: String): Double? {
        if (amountStr.isBlank()) {
            Toast.makeText(this, "Por favor, insira um valor.", Toast.LENGTH_SHORT).show()
            return null
        }
        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            Toast.makeText(this, "Valor inválido. Insira um número positivo.", Toast.LENGTH_SHORT).show()
            return null
        }
        return amount
    }

    private suspend fun updateMoneyWalletBalance(withdrawAmount: Double) {
        val wallet = walletDao.findById(1).firstOrNull() ?: return
        val updatedWallet = wallet.copy(totalBalance = wallet.totalBalance - withdrawAmount)
        walletDao.updateWallet(updatedWallet)
    }

    private suspend fun loadMoneyWalletBalance() {
        val moneyWalletBalance = walletDao.findById(1).firstOrNull()?.totalBalance ?: 0.0
        val formattedBalance = formatValueDollarCurrency(moneyWalletBalance.toString())
        binding.tvActivityWithdrawFormBalance.text = formattedBalance
    }
}