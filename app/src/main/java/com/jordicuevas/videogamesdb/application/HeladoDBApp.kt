package com.jordicuevas.videogamesdb.application

import android.app.Application
import com.jordicuevas.videogamesdb.data.HeladoRepository
import com.jordicuevas.videogamesdb.data.db.HeladoDatabase

class HeladoDBApp: Application() {
    private val database by lazy{
        HeladoDatabase.getDatabase(this@HeladoDBApp)

    }
    val repository by lazy {
        HeladoRepository(database.heladoDao())
    }
}