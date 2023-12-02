package br.com.povengenharia.simuladorcarteiracrypto.database.webclient

import br.com.povengenharia.simuladorcarteiracrypto.database.webclient.service.CoinService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import br.com.povengenharia.simuladorcarteiracrypto.BuildConfig

val apiKey = BuildConfig.COINRANKING_API_KEY

val apiKeyInterceptor = Interceptor { chain ->
    val originalRequest = chain.request()
    val newUrl = originalRequest.url.newBuilder()
        .addQueryParameter("apikey", apiKey)
        .build()
    val newRequest = originalRequest.newBuilder()
        .url(newUrl)
        .build()
    chain.proceed(newRequest)
}


val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(apiKeyInterceptor)
    .build()


class StarterRetrofit {

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl("https://api.coinranking.com/v2/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val coinService = retrofit.create(CoinService::class.java)
}