package br.com.povengenharia.simuladorcarteiracrypto.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_7_8 = object : Migration(7, 8){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE CryptoFromApi ADD COLUMN 'isFavorite' INTEGER NOT NULL DEFAULT 0")
    }

}

val MIGRATION_8_9 = object : Migration(8,9){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE CryptoFromApi ADD COLUMN 'timePeriod' TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_9_10 = object : Migration(9,10){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE CryptoFromApi_new (uuid TEXT PRIMARY KEY NOT NULL, symbol TEXT, name TEXT, iconUrl TEXT, price TEXT, isFavorite INTEGER NOT NULL, change TEXT NOT NULL DEFAULT '')")

        db.execSQL("INSERT INTO CryptoFromApi_new (uuid, symbol, name, iconUrl, price, isFavorite, change) SELECT uuid, symbol, name, iconUrl, price, isFavorite, timePeriod FROM CryptoFromApi")

        db.execSQL("DROP TABLE CryptoFromApi")

        db.execSQL("ALTER TABLE CryptoFromApi_new RENAME TO CryptoFromApi")


    }

}