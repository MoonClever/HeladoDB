package com.jordicuevas.videogamesdb.ui

import androidx.recyclerview.widget.RecyclerView
import com.jordicuevas.videogamesdb.data.db.model.HeladoEntity
import com.jordicuevas.videogamesdb.databinding.GameElementBinding

class HeladoViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

    val ivIcon = binding.ivHelado

    fun bind(game: HeladoEntity){
        binding.apply{
            tvNombre.text = game.sabor
            tvMarca.text = game.marca
            tvTamano.text = game.tamano
        }

    }

}