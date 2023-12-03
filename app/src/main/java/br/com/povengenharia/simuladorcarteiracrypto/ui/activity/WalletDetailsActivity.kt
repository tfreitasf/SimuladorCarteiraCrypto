package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityWalletDetailsBinding

class WalletDetailsActivity : AppCompatActivity() {



    private val binding by lazy {
        ActivityWalletDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureBuyFab()

    }

    private fun configureBuyFab() {
        val fab = binding.fabActivityWalletDetailsBuyCrypto
        fab.setOnClickListener {
            cryptoListActivity()
        }
    }

    private fun cryptoListActivity() {
        val intent = Intent(this, CryptoListActivity::class.java)
        startActivity(intent)
    }


}