package com.robert.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robert.database.dao.CardDao
import com.robert.database.dao.TransactionDao
import com.robert.database.dao.UserProfileDao
import com.robert.database.entity.CardEntity
import com.robert.database.entity.TransactionEntity
import com.robert.database.entity.UserProfileEntity
import com.robert.database.entity.WalletEntity

@Database(
    entities = [
        CardEntity::class,
        WalletEntity::class,
        TransactionEntity::class,
        UserProfileEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun transactionDao(): TransactionDao
    abstract fun userProfileDao(): UserProfileDao
}
