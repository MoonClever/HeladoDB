package com.jordicuevas.videogamesdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jordicuevas.videogamesdb.data.db.model.HeladoEntity
import com.jordicuevas.videogamesdb.databinding.GameElementBinding

class HeladoAdapter(private val onGameClicked: (HeladoEntity) -> Unit): RecyclerView.Adapter<HeladoViewHolder>(){

    private var helados: List<HeladoEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeladoViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeladoViewHolder(binding)
    }

    override fun getItemCount(): Int = helados.size

    override fun onBindViewHolder(holder: HeladoViewHolder, position: Int) {

        val helado = helados[position]

        holder.bind(helado)


        holder.itemView.setOnClickListener{
            onGameClicked(helado)
        }


    }
    //Para actualizar el recyclerview
    fun updateList(list: List<HeladoEntity>){
        helados = list
        notifyDataSetChanged()

    }

}