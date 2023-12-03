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
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityCryptoListBinding
import br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter.CoinListAdapter
import kotlinx.coroutines.launch

class CryptoListActivity : AppCompatActivity() {

    private lateinit var adapter: CoinListAdapter

    private val apikey = BuildConfig.COINRANKING_API_KEY

    private val coinService by lazy {
        initializeCoinService()
    }

    private val binding by lazy{
        ActivityCryptoListBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adapter = CoinListAdapter(emptyList())
        setupRecyclerView()
        fetchCoins()
    }

    private fun setupRecyclerView(){
        binding.rvActivityCryptoListRecyclerview.apply {
            adapter = this@CryptoListActivity.adapter
            layoutManager = LinearLayoutManager(this@CryptoListActivity)
        }

    }

    private fun fetchCoins() {
        lifecycleScope.launch {
            try {
                val response = coinService.getCoins(apikey)
                if (response.isSuccessful) {
                    val coins = response.body()?.data?.coins?.take(20) ?: emptyList()
                    adapter.updateList(coins)
                } else {
                    Toast.makeText(this@CryptoListActivity, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CryptoListActivity, "Erro ao buscar dados", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Erro ao buscar criptomoedas", e)
            }
        }
    }


    private fun initializeCoinService(): CoinService {
        val starterRetrofit = StarterRetrofit()
        return starterRetrofit.coinService
    }
}