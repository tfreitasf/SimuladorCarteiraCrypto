package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityCryptoListBinding
import br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter.CoinListAdapter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CryptoListActivity : AppCompatActivity() {

    private lateinit var adapter: CoinListAdapter


    private val binding by lazy {
        ActivityCryptoListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        fetchCryptosFromDatabase()
    }

    private fun setupRecyclerView() {
        adapter = CoinListAdapter(emptyList(), this)
        binding.rvActivityCryptoListRecyclerview.apply {
            adapter = this@CryptoListActivity.adapter
            layoutManager = LinearLayoutManager(this@CryptoListActivity)
        }
    }

    private fun fetchCryptosFromDatabase() {
        lifecycleScope.launch {
            val cryptoFromApiDao =
                AppDatabase.getInstance(this@CryptoListActivity, lifecycleScope).cryptoFromApiDao()
            val cryptos = cryptoFromApiDao.getAllCryptos().firstOrNull() ?: emptyList()
            adapter.updateList(cryptos)
        }
    }
}