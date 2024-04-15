package com.jordicuevas.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.jordicuevas.videogamesdb.R
import com.jordicuevas.videogamesdb.application.HeladoDBApp
import com.jordicuevas.videogamesdb.data.HeladoRepository
import com.jordicuevas.videogamesdb.data.db.model.HeladoEntity
import com.jordicuevas.videogamesdb.databinding.GameDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HeladoDialog(
    private val newHelado: Boolean = true,
    private var helado: HeladoEntity = HeladoEntity(
        marca = "",
        sabor = "",
        tamano = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment() {

    private var _binding: GameDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: HeladoRepository


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        builder = AlertDialog.Builder(requireContext())

        repository = (requireContext().applicationContext as HeladoDBApp).repository

        binding.apply{
            binding.spSabor.setSelection(setIDSpinner(helado.sabor))
            binding.tietMarca.setText(helado.marca)
            binding.tietTamano.setText(helado.tamano)
        }

        dialog = if(newHelado)
            buildDialog(getString(R.string.guardar_string), getString(R.string.cancelar_string), {
                  //Accion guardar
                helado.apply{
                    marca = binding.spSabor.selectedItem.toString()
                    sabor = binding.tietMarca.text.toString()
                    tamano = binding.tietTamano.text.toString()
                }

                try {
                    lifecycleScope.launch {
                        repository.insertHelado(helado)
                    }
                    message(getString(R.string.exito_insert_string))
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.error_insert_string))
                    updateUI()
                }
            }, {
                //Accion cancelar
            })
        else
            buildDialog(getString(R.string.actualizar_string), getString(R.string.borrar_string), {
                  //Accion actualizar
                helado.apply{
                    marca = binding.spSabor.selectedItem.toString()
                    sabor = binding.tietMarca.text.toString()
                    tamano = binding.tietTamano.text.toString()
                }

                try {
                    lifecycleScope.launch {
                        repository.updateHelado(helado)
                    }
                    message(getString(R.string.exito_update_string))
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.error_upload_string))
                    updateUI()
                }
            }, {
                 //Accion borrar
                val context = requireContext()
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirmacion_string))
                    .setMessage(getString(R.string.Alert_confirmation_string, helado.marca))
                    .setPositiveButton(getString(R.string.aceptar_string)){ _, _ ->
                        try {
                            lifecycleScope.launch {
                                val result = async {
                                    repository.deleteHelado(helado)
                                }

                                result.await()

                                withContext(Dispatchers.Main){
                                    message(context.getString(R.string.exito_delete_string))
                                    updateUI()
                                }


                            }




                        }catch(e: IOException) {
                            e.printStackTrace()
                            message(getString(R.string.error_delete_string))
                        }
                    }
                    .setNegativeButton(getString(R.string.cancelar_string)){ dialog, _ ->
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


        binding.tietMarca.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietTamano.addTextChangedListener(object: TextWatcher {
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
        (       binding.tietMarca.text.toString().isNotEmpty() &&
                binding.tietMarca.text.toString().isNotEmpty())


    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.app_name))
            .setPositiveButton(btn1Text){ _, _ ->
                ///Accion para el boton positivo
                positiveButton()
            }
            .setNegativeButton(btn2Text){ _, _ ->
                //Accion para el boton negativo
                negativeButton()
            }
            .create()

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