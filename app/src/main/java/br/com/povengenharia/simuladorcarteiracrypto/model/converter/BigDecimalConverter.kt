package br.com.povengenharia.simuladorcarteiracrypto.model.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String = value?.toString() ?: "0.0"

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal = if (value.isEmpty()) BigDecimal.ZERO else BigDecimal(value)
}