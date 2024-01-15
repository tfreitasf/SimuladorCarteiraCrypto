package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.povengenharia.simuladorcarteiracrypto.R
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.database.local.dao.WalletDao
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityWalletFormBinding
import br.com.povengenharia.simuladorcarteiracrypto.model.Wallet
import kotlinx.coroutines.launch
import java.math.BigDecimal

class WalletFormActivity : AppCompatActivity() {

    private var walletId = 0

    private val walletDao: WalletDao by lazy {
        val db = AppDatabase.getInstance(this, lifecycleScope)
        db.walletDao()
    }

    private val binding by lazy {
        ActivityWalletFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        saveNewWallet()

        intent.getParcelableExtra<Wallet>(KEY_WALLET_ID)?.let { walletLoad ->
            binding.tvactivityWalletFormTitle.setText(getString(R.string.txt_wallet_form_activity_edit_wallet))
            walletId = walletLoad.id
            binding.etActivityWalletFormName.setText(walletLoad.name)
            binding.etActivityWalletFormDescription.setText(walletLoad.description)
        }
    }


    private fun saveNewWallet() {
        val btnSave = binding.btActivityWalletFormSave
        btnSave.setOnClickListener {

            val wallet = createNewWalletFromForm()
            lifecycleScope.launch {
                if (walletId > 0) {
                    walletDao.updateWallet(wallet)
                } else {
                    walletDao.addWallet(wallet)
                }
                finish()
            }
        }
    }

    private fun createNewWalletFromForm(): Wallet {
        val nameField = binding.etActivityWalletFormName
        val descriptionField = binding.etActivityWalletFormDescription

        val name = nameField.text.toString()
        val description = descriptionField.text.toString()

        return Wallet(
            id = walletId,
            name = name,
            description = description,
            type = "Crypto",
            totalBalance = BigDecimal.ZERO
        )
    }
}