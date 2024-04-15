package com.jordicuevas.videogamesdb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jordicuevas.videogamesdb.data.db.model.HeladoEntity
import com.jordicuevas.videogamesdb.util.Constants

@Dao
interface HeladoDao {

    //Create
    @Insert
    suspend fun insertHelado(helado: HeladoEntity)

    @Insert
    suspend fun insertHelado(helado: List<HeladoEntity>)

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_GAME_TABLE}")
    suspend fun getAllHelados(): List<HeladoEntity>

    //Update
    @Update
    suspend fun updateHelado(helado: HeladoEntity)

    //Delete
    @Delete
    suspend fun deleteHelado(game: HeladoEntity)


}