package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

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
    }
}