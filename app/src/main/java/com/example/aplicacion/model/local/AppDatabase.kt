package com.example.aplicacion.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UsuarioEntity::class, ProductoEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 3. Declaramos ambos DAOs para que la base de datos sepa cómo acceder a ellos
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 4. Elegimos un nombre unificado para la base de datos
        private const val DATABASE_NAME = "app_database.db"

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // 5. Añadimos fallbackToDestructiveMigration para manejar el cambio de versión
                    // Esto borrará la base de datos anterior y creará una nueva. Es lo más simple durante el desarrollo.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
