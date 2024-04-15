package com.jordicuevas.videogamesdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jordicuevas.videogamesdb.data.GameRepository
import com.jordicuevas.videogamesdb.data.db.model.GameEntity
import com.jordicuevas.videogamesdb.databinding.GameElementBinding

class GameAdapter(private val onGameClicked: (GameEntity) -> Unit): RecyclerView.Adapter<GameViewHolder>(){

    private var games: List<GameEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {

        val game = games[position]

        holder.bind(game)


        holder.itemView.setOnClickListener{
            onGameClicked(game)
        }


    }
    //Para actualizar el recyclerview
    fun updateList(list: List<GameEntity>){
        games = list
        notifyDataSetChanged()

    }

}