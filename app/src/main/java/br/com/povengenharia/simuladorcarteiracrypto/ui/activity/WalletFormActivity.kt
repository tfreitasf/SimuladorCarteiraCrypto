package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityWalletFormBinding

class WalletFormActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityWalletFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}