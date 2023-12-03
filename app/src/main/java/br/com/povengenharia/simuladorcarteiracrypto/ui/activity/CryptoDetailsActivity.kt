package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityCryptoDetailsBinding

class CryptoDetailsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCryptoDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}