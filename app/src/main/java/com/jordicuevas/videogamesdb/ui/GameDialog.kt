package com.jordicuevas.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.jordicuevas.videogamesdb.application.VideoGamesDBApp
import com.jordicuevas.videogamesdb.data.GameRepository
import com.jordicuevas.videogamesdb.data.db.model.GameEntity
import com.jordicuevas.videogamesdb.databinding.GameDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException

class GameDialog(
    private val newGame: Boolean = true,
    private var game: GameEntity = GameEntity(
        title = "",
        genre = "",
        developer = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment() {

    private var _binding: GameDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: GameRepository


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        builder = AlertDialog.Builder(requireContext())

        repository = (requireContext().applicationContext as VideoGamesDBApp).repository

        binding.apply{
            binding.tietTitle.setText(game.title)
            binding.tietGenre.setText(game.genre)
            binding.tietDeveloper.setText(game.developer)
        }

        dialog = if(newGame)
            buildDialog("Guardar", "Cancelar", {
                  //Accion guardar
                game.apply{
                    title = binding.tietTitle.text.toString()
                    genre = binding.tietGenre.text.toString()
                    developer = binding.tietDeveloper.text.toString()
                }

                try {
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }
                    message("Juego insertado exitosamente")
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al guardar juego")
                    updateUI()
                }
            }, {
                //Accion cancelar
            })
        else
            buildDialog("Actualizar", "Borrar", {
                  //Accion actualizar
                game.apply{
                    title = binding.tietTitle.text.toString()
                    genre = binding.tietGenre.text.toString()
                    developer = binding.tietDeveloper.text.toString()
                }

                try {
                    lifecycleScope.launch {
                        repository.updateGame(game)
                    }
                    message("Juego actualizado exitosamente")
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al actualizar al juego")
                    updateUI()
                }
            }, {
                 //Accion borrar

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Realmente deseas eliminar el juego ${game.title}?")
                    .setPositiveButton("Aceptar"){ _, _ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteGame(game)
                            }

                            message("Juego borrado exitosamente")
                            updateUI()
                        }catch(e: IOException) {
                            e.printStackTrace()
                            message("Error al borrar juego")
                        }
                    }
                    .setNegativeButton("Cancelar"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()


            })



        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        val alertDialog = dialog as AlertDialog

        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

//        binding.tietTitle.setOnFocusChangeListener { v, hasFocus ->
//            if(!hasFocus){
//                ///Aqu[i ya perdio el foco
//            }
//        }

        binding.tietTitle.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietGenre.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietDeveloper.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

    }


    private fun validateFields(): Boolean =
        (binding.tietTitle.text.toString().isNotEmpty() &&
                binding.tietGenre.text.toString().isNotEmpty() &&
                binding.tietDeveloper.text.toString().isNotEmpty())


    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Juego")
            .setPositiveButton(btn1Text){ _, _ ->
                ///Accion para el boton positivo
                positiveButton()
            }
            .setNegativeButton(btn2Text){ _, _ ->
                //Accion para el boton negativo
                negativeButton()
            }
            .create()

}