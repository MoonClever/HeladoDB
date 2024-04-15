package com.jordicuevas.videogamesdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jordicuevas.videogamesdb.data.db.model.HeladoEntity
import com.jordicuevas.videogamesdb.util.Constants

@Database(
    entities = [HeladoEntity::class],
    version = 1,
    exportSchema = true
)

abstract class HeladoDatabase: RoomDatabase() {
    abstract fun heladoDao(): HeladoDao

    companion object {

        @Volatile
        private var INSTANCE: HeladoDatabase? = null

        fun getDatabase(context: Context): HeladoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HeladoDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }

    }

    }