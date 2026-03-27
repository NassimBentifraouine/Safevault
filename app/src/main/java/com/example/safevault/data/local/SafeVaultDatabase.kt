package com.example.safevault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.safevault.data.local.dao.DocumentsDao
import com.example.safevault.data.local.entity.DocumentEntity

@Database(
    entities = [DocumentEntity::class],
    version = 4,
    exportSchema = true,
)
abstract class SafeVaultDatabase : RoomDatabase() {
    abstract fun documentsDao(): DocumentsDao

    companion object {
        const val DATABASE_NAME = "safevault.db"

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS documents_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        category TEXT NOT NULL,
                        note TEXT NOT NULL,
                        imageUri TEXT,
                        createdAtMillis INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    INSERT INTO documents_new (id, title, category, note, imageUri, createdAtMillis)
                    SELECT id, title, category, note, imageUri, createdAtMillis FROM documents
                    """.trimIndent(),
                )
                db.execSQL("DROP TABLE documents")
                db.execSQL("ALTER TABLE documents_new RENAME TO documents")
            }
        }

        // Schema unchanged between v2 and v4 for the documents table.
        val MIGRATION_2_4: Migration = object : Migration(2, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // no-op
            }
        }

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS document_audits")
            }
        }
    }
}
