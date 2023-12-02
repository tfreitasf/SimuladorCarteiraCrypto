package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.povengenharia.simuladorcarteiracrypto.BuildConfig
import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.StarterRetrofit
import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.service.CoinService
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityMainBinding
import br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter.CoinListAdapter
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: CoinListAdapter

    private val apiKey = BuildConfig.COINRANKING_API_KEY

    private val coinService by lazy {
        initializeCoinService()
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adapter = CoinListAdapter(emptyList())
        setupRecyclerView()
        fetchCoins()
    }

    private fun setupRecyclerView() {
        binding.rvMainActivityRecyclerview.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }

    private fun fetchCoins() {
        lifecycleScope.launch {
            try {
                val response = coinService.getCoins(apiKey)
                if (response.isSuccessful) {
                    val coins = response.body()?.data?.coins?.take(20) ?: emptyList()
                    adapter.updateList(coins)
                } else {
                    Toast.makeText(this@MainActivity, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Erro ao buscar dados", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Erro ao buscar criptomoedas", e)
            }
        }
    }

    private fun initializeCoinService(): CoinService {
        val starterRetrofit = StarterRetrofit()
        return starterRetrofit.coinService
    }


}