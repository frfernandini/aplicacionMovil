package com.example.aplicacion.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDAO

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =

            INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(

                    context.applicationContext,
                    AppDatabase::class.java,
                    "productos.db"

                ).build().also { INSTANCE = it }

            }
    }
}