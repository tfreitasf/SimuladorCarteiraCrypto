package br.com.povengenharia.simuladorcarteiracrypto.database.webclient.service

import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.model.CoinResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinService {

    @GET("coins")
    suspend fun getCoins(@Query("apikey") apiKey: String): Response<CoinResponse>
}