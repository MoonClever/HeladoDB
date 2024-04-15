package com.jordicuevas.videogamesdb.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.jordicuevas.videogamesdb.R
import com.jordicuevas.videogamesdb.application.HeladoDBApp
import com.jordicuevas.videogamesdb.data.HeladoRepository
import com.jordicuevas.videogamesdb.data.db.model.HeladoEntity
import com.jordicuevas.videogamesdb.databinding.ActivityRegistroHeladoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class RegistroHelado : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroHeladoBinding

    private var helado: HeladoEntity = HeladoEntity(
        sabor = "",
        marca = "",
        tamano = ""
    )

    private lateinit var repository: HeladoRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroHeladoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as HeladoDBApp).repository

        binding.btAgregar.setOnClickListener {
            helado.apply {
                sabor = binding.spSabor.selectedItem.toString()
                marca = binding.etMarca.text.toString()
                tamano = binding.etTamano.text.toString() }

            if (binding.etMarca.text.isEmpty() || binding.etTamano.text.isEmpty()){

                message(getString(R.string.aviso_vacio_string))

            }else {

                try {
                    lifecycleScope.launch {

                        val result = async {
                            repository.insertHelado(helado)
                        }

                        result.await()

                        withContext(Dispatchers.Main) {
                            message(getString(R.string.exito_insert_string))

                        }

                    }

                } catch (e: IOException) {
                    e.printStackTrace()

                    message(getString(R.string.error_insert_string))

                }
            }



        }


        binding.btRecyclerView.setOnClickListener {
            val rvHelados = Intent(this, MainActivity::class.java)
            startActivity(rvHelados)
        }



    }

    private fun message(text: String) {
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        Snackbar.make(binding.registro, text, Snackbar.LENGTH_SHORT)
            .setTextColor(resources.getColor(R.color.background_white, theme))
            .setBackgroundTint(resources.getColor(R.color.pink, theme))
            .show()

    }

    private fun setIDSpinner(selection: String): Int {
        var id = 0
        when(selection){
            "Chocolate" -> id = 0
            "Fresa" -> id = 1
            "Vainilla" -> id =  2
            "Napolitano" -> id =  3
        }
        return id
    }

}