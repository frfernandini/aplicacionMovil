package com.example.aplicacion.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UsuarioEntity::class, ProductoEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {


    abstract fun usuarioDao(): UsuarioDao
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
