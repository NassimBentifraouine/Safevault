package com.example.safevault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.safevault.data.local.dao.DocumentsDao
import com.example.safevault.data.local.entity.DocumentEntity

@Database(
    entities = [DocumentEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class SafeVaultDatabase : RoomDatabase() {
    abstract fun documentsDao(): DocumentsDao

    companion object {
        const val DATABASE_NAME = "safevault.db"
    }
}
