package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityWalletDetailsBinding
import kotlinx.coroutines.launch

class WalletDetailsActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityWalletDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val walletID = intent.getIntExtra(KEY_WALLET_ID, -1)
        Toast.makeText(this, "ID da carteira recebida: $walletID", Toast.LENGTH_LONG).show()


    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            configureBuyFab()
        }
    }


    private fun configureBuyFab() {
        val fab = binding.fabActivityWalletDetailsBuyCrypto
        fab.setOnClickListener {

            cryptoListActivity()
        }
    }

    private fun cryptoListActivity() {
        val walletID = intent.getIntExtra(KEY_WALLET_ID, -1)
        val intent = Intent(this, CryptoListActivity::class.java).apply {
            putExtra(KEY_WALLET_ID, walletID)
        }
        startActivity(intent)

    }
}