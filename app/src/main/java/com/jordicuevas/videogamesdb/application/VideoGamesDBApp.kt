package com.jordicuevas.videogamesdb.application

import android.app.Application
import com.jordicuevas.videogamesdb.data.GameRepository
import com.jordicuevas.videogamesdb.data.db.GameDatabase

class VideoGamesDBApp: Application() {
    private val database by lazy{
        GameDatabase.getDatabase(this@VideoGamesDBApp)

    }
    val repository by lazy {
        GameRepository(database.gameDao())
    }
}