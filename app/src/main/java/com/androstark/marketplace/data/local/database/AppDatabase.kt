package com.androstark.marketplace.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.androstark.marketplace.data.local.dao.WishlistDao
import com.androstark.marketplace.data.local.entity.WishlistItem

@Database(
    entities = [WishlistItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun wishlistDao(): WishlistDao
    
    companion object {
        const val DATABASE_NAME = "marketplace_database"
    }
}
