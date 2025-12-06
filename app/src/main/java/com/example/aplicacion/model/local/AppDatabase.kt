package com.example.aplicacion.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// PASO 1: Añadir UsuarioEntity a la lista de entidades
@Database(entities = [UsuarioEntity::class, ProductoEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // PASO 2: Añadir la función abstracta para el nuevo DAO
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun productoDao(): ProductoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DATABASE_NAME = "app_database.db"

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
