package com.example.safevault.di

import android.content.Context
import androidx.room.Room
import com.example.safevault.data.local.SafeVaultDatabase
import com.example.safevault.data.repository.DocumentsRepository
import com.example.safevault.data.repository.RoomDocumentsRepository

interface AppContainer {
    val documentsRepository: DocumentsRepository
}

class DefaultAppContainer(
    private val context: Context,
) : AppContainer {
    private val database: SafeVaultDatabase by lazy {
        Room.databaseBuilder(
            context = context,
            klass = SafeVaultDatabase::class.java,
            name = SafeVaultDatabase.DATABASE_NAME,
        ).addMigrations(
            SafeVaultDatabase.MIGRATION_1_2,
            SafeVaultDatabase.MIGRATION_2_4,
            SafeVaultDatabase.MIGRATION_3_4,
        )
            .build()
    }

    override val documentsRepository: DocumentsRepository by lazy {
        RoomDocumentsRepository(
            documentsDao = database.documentsDao(),
        )
    }
}
