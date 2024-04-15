package com.jordicuevas.videogamesdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jordicuevas.videogamesdb.util.Constants

@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class HeladoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "helado_id")
    val id: Long = 0,
    @ColumnInfo(name = "helado_sabor")
    var sabor: String,
    @ColumnInfo(name = "helado_marca")
    var marca: String,
    @ColumnInfo(name = "game_developer", defaultValue = "Desconocido")
    var tamano: String
)
