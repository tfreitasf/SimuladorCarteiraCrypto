package br.com.povengenharia.simuladorcarteiracrypto.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.povengenharia.simuladorcarteiracrypto.database.AppDatabase
import br.com.povengenharia.simuladorcarteiracrypto.databinding.ActivityCryptoListBinding
import br.com.povengenharia.simuladorcarteiracrypto.model.CryptoFromApi
import br.com.povengenharia.simuladorcarteiracrypto.ui.recyclerview.adapter.CoinListAdapter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CryptoListActivity : AppCompatActivity() {

    private lateinit var adapter: CoinListAdapter


    private val binding by lazy {
        ActivityCryptoListBinding.inflate(layoutInflater)
    }

    private val cryptoFromApiDao by lazy {
        AppDatabase.getInstance(this, lifecycleScope).cryptoFromApiDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val walletId = intent.getIntExtra(KEY_WALLET_ID, -1)
        setupRecyclerView(walletId)
        fetchCryptosFromDatabase()
        setupSearch()
    }

    private fun setupRecyclerView(walletId: Int) {
        val walletId = intent.getIntExtra(KEY_WALLET_ID, -1)
        adapter = CoinListAdapter(
            mutableListOf(), this, walletId, { crypto ->
                val intent = Intent(this, CryptoDetailsActivity::class.java).apply {
                    putExtra("EXTRA_COIN_UUID", crypto.uuid)
                    putExtra(KEY_WALLET_ID, walletId)
                }
                startActivity(intent)
            },
            { crypto ->
                updateFavoriteStatus(crypto)
            }
        )
        binding.rvActivityCryptoListRecyclerview.apply {
            adapter = this@CryptoListActivity.adapter
            layoutManager = LinearLayoutManager(this@CryptoListActivity)
        }
    }

    private fun setupSearch() {
        binding.etActivityCryptoListSearch.addTextChangedListener { editable ->
            val searchText = editable?.toString() ?: ""
            performSearch(searchText)

        }
    }

    private fun performSearch(query: String) {
        lifecycleScope.launch {
            val searchQuery = "%$query%"
            val cryptoFromApiDao =
                AppDatabase.getInstance(this@CryptoListActivity, lifecycleScope).cryptoFromApiDao()
            cryptoFromApiDao.searchCryptos(searchQuery)
                .map { cryptos -> cryptos.sortedByDescending { it.isFavorite } }
                .collect { filteredCryptos ->
                    adapter.updateList(filteredCryptos)
                }
        }
    }

    private fun updateFavoriteStatus(crypto: CryptoFromApi) {
        lifecycleScope.launch {
            cryptoFromApiDao.updateCrypto(crypto)
            fetchCryptosFromDatabase()
            adapter.notifyDataSetChanged()
        }
    }


    private fun fetchCryptosFromDatabase() {
        lifecycleScope.launch {
            val cryptoFromApiDao =
                AppDatabase.getInstance(this@CryptoListActivity, lifecycleScope).cryptoFromApiDao()
            val cryptos = cryptoFromApiDao.getAllCryptosOrderedByFavorite().firstOrNull() ?: emptyList()
            adapter.updateList(cryptos)
        }
    }
}