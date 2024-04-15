package com.jordicuevas.videogamesdb.data

import com.jordicuevas.videogamesdb.data.db.HeladoDao
import com.jordicuevas.videogamesdb.data.db.model.HeladoEntity

class HeladoRepository(private val heladoDao: HeladoDao) {

    suspend fun insertHelado(helado: HeladoEntity){
        heladoDao.insertHelado(helado)
    }

    suspend fun getAllGames(): List<HeladoEntity>{
        return heladoDao.getAllHelados()
    }

    suspend fun updateHelado(helado: HeladoEntity){
        heladoDao.updateHelado(helado)
    }

    suspend fun deleteHelado(helado: HeladoEntity){
        heladoDao.deleteHelado(helado)
    }
}