package com.example.levelupgamer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [UsuarioEntity::class, CategoryEntity::class, ProductEntity::class,CompraEntity::class, CompraLineaEntity::class  ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun compraDao(): CompraDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "levelup.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SeedCallback(context.applicationContext))
                    .build()
                    .also { INSTANCE = it }
            }
    }

    private class SeedCallback(private val ctx: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Sembrar en hilo de IO
            CoroutineScope(Dispatchers.IO).launch {
                val instance = getInstance(ctx)
                seed(instance.categoryDao(), instance.productDao())
            }
        }
    }
}
