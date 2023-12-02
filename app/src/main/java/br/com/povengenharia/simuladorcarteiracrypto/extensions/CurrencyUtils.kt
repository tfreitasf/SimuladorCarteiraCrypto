package br.com.povengenharia.simuladorcarteiracrypto.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun formatValueDollarCurrency(price: String): String? {
    val priceBigDecimal = BigDecimal(price)
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val roundedPrice = priceBigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN)
    return currencyFormat.format(roundedPrice)
}