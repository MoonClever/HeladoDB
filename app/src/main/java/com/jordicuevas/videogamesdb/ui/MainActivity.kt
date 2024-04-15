package com.jordicuevas.videogamesdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jordicuevas.videogamesdb.R
import com.jordicuevas.videogamesdb.application.VideoGamesDBApp
import com.jordicuevas.videogamesdb.data.GameRepository
import com.jordicuevas.videogamesdb.data.db.model.GameEntity
import com.jordicuevas.videogamesdb.databinding.ActivityMainBinding
import com.jordicuevas.videogamesdb.util.Constants
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private var games: List<GameEntity> = emptyList()
    private lateinit var repository: GameRepository

    private lateinit var gameAdapter: GameAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as VideoGamesDBApp).repository

        gameAdapter = GameAdapter(){ selectedGame ->
            val dialog = GameDialog(
                newGame = false,
                game = selectedGame,
                updateUI = { updateUI()
                },message = { action ->
                  message(action)
                })

            dialog.show(supportFragmentManager, "UpdateDialog")
        }

        binding.rvGames.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = gameAdapter
        }

        updateUI()

//        var url = Constants.BASE_URL
//
//        Log.d(Constants.LOGTAG, "Hola")
    }

    private fun updateUI(){
        lifecycleScope.launch() {
            games = repository.getAllGames()

            binding.tvSinRegistros.visibility =
                if(games.isEmpty()) View.VISIBLE else View.INVISIBLE

            gameAdapter.updateList(games)
        }
    }

    fun click(view: View) {
        //Manejo de el click del FAB
        val dialog = GameDialog(
            updateUI = {
                updateUI()
            }, message = {action ->
                message(action)
         })
        dialog.show(supportFragmentManager, "InsertDialog")
    }

    private fun message(text: String){
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()
    }
}