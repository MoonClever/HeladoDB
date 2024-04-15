package com.jordicuevas.videogamesdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jordicuevas.videogamesdb.R
import com.jordicuevas.videogamesdb.application.HeladoDBApp
import com.jordicuevas.videogamesdb.data.HeladoRepository
import com.jordicuevas.videogamesdb.data.db.model.HeladoEntity
import com.jordicuevas.videogamesdb.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private var helados: List<HeladoEntity> = emptyList()
    private lateinit var repository: HeladoRepository

    private lateinit var heladoAdapter: HeladoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as HeladoDBApp).repository

        heladoAdapter = HeladoAdapter(){ selectedHelado ->
            val dialog = HeladoDialog(
                newHelado = false,
                helado = selectedHelado,
                updateUI = { updateUI()
                },message = { action ->
                  message(action)
                })

            dialog.show(supportFragmentManager, "UpdateDialog")
        }

        binding.rvGames.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = heladoAdapter
        }

        updateUI()

//        var url = Constants.BASE_URL
//
//        Log.d(Constants.LOGTAG, "Hola")
    }

    private fun updateUI(){
        lifecycleScope.launch() {
            helados = repository.getAllGames()

            binding.tvSinRegistros.visibility =
                if(helados.isEmpty()) View.VISIBLE else View.INVISIBLE

            heladoAdapter.updateList(helados)
        }
    }

    fun click(view: View) {
        //Manejo de el click del FAB
        val dialog = HeladoDialog(
            updateUI = {
                updateUI()
            }, message = {action ->
                message(action)
         })
        dialog.show(supportFragmentManager, "InsertDialog")
    }

    private fun message(text: String) {
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(resources.getColor(R.color.background_white, theme))
            .setBackgroundTint(resources.getColor(R.color.pink, theme))
            .show()

    }
}