package com.carlasarai.TicketsKotlin.ui.obtenerCsv

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.carlasarai.TicketsKotlin.ui.menuLateral.MenuActivity
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.pruebanueva.R
import com.carlasarai.pruebanueva.databinding.ActivityObtenerCsvBinding
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ObtenerCsvActivity : AppCompatActivity() {

    private lateinit var binding: ActivityObtenerCsvBinding
    private val obtenerCsvViewModel: ObtenerCsvViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 123
    private var permissionsExplanationShown = false
    private val importCsvLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Notificar al ViewModel sobre la selección del archivo
                    obtenerCsvViewModel.onSeleccionArchivo(uri, this)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObtenerCsvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initListeners()
        obtenerCsvViewModel.getTicks.observe(this) { ticketList ->
            if (ticketList.isNotEmpty()) {
                obtenerCsvViewModel.isLoading.postValue(false)
                obtenerCsvViewModel.overlayView.postValue(false)
                getToBuscarTicket()
                finish()
            }
       }
    }
    override fun onStart() {
        super.onStart()
        if (!obtenerCsvViewModel.hasStoragePermission(this)){
            permissionsExplanationShown = false
        }
        obtenerCsvViewModel.permissionReadExternalStorage(this)
        obtenerCsvViewModel.encargada.observe(this){
            if (it.isEmpty()){
                mostrarDialogo()
            }
        }
    }
    private fun initListeners() {
        binding.buttonImportarDocumento.setOnClickListener {
            if (obtenerCsvViewModel.isLoading.value == false){
                if (obtenerCsvViewModel.hasStoragePermission(this)) {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "text/*"
                    println(intent.type)
                    importCsvLauncher.launch(intent)
                }else{
                    obtenerCsvViewModel.permissionReadExternalStorage(this)
                }
            }
        }
        binding.buttonDescargarDocumento.setOnClickListener {
            if(obtenerCsvViewModel.isLoading.value == false){
                getToBuscarTicket()
            }
        }
    }

    private fun initUI(){
        obtenerCsvViewModel.isLoading.observe(this, Observer {
            binding.progress.isVisible = it
        })
        obtenerCsvViewModel.overlayView.observe(this, Observer {
            binding.overlayView.isVisible = it
        })
    }

    fun getToBuscarTicket() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish() // Finalizar la actividad actual (obtenerCsv)
    }

    private fun mostrarDialogo() {
        val dialogo = Dialog(this)
        dialogo.setContentView(R.layout.dialogo_iniciar_sesion)
        dialogo.setCancelable(false)
        val etUsuario = dialogo.findViewById<TextInputEditText>(R.id.etUsuario)
        val etArea = dialogo.findViewById<AutoCompleteTextView>(R.id.etNombreArea)
        //val etFecha = dialogo.findViewById<AppCompatEditText>(R.id.etFecha)
        val buttonConfirmar = dialogo.findViewById<AppCompatButton>(R.id.buttonConfirmar)
        val areasss = resources.getStringArray(R.array.nombre_area_comedor)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item, areasss)
        val formato = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val formatoHora = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault())
        val fechaActual = Calendar.getInstance().time
        val fechaFormateada = formato.format(fechaActual)
        val editableFecha = Editable.Factory.getInstance().newEditable(fechaFormateada)
        dialogo.findViewById<AutoCompleteTextView>(R.id.etFecha).text = editableFecha
        etArea.setAdapter(arrayAdapter)
        buttonConfirmar.setOnClickListener {
            when{
                etUsuario.text.toString().isBlank() -> Toast.makeText(this, "Por favor, ingresa tu nombre completo.", Toast.LENGTH_SHORT).show()
                etUsuario.text.toString().split(" ").size < 3 -> Toast.makeText(this, "Ingresa tu nombre completo, incluyendo los apellidos.", Toast.LENGTH_SHORT).show()
                //etFecha.text.toString().isBlank() -> Toast.makeText(this, "Por favor, ingresa la fecha.", Toast.LENGTH_SHORT).show()
                else -> {
                    // Todos los criterios de validación han pasado, procede con la confirmación
                    val dialog = AlertDialog.Builder(this)
                        .setMessage("¿Estás seguro de que los datos ${etUsuario.text.toString()} y ${etArea.text.toString()} son correctos?")
                        .setPositiveButton("Sí") { _, _ ->
                            val nombreEncargada = etUsuario.text.toString()
                            val nombreArea = etArea.text.toString()
                            //val fechaActual = etFecha.text.toString()
                            val encargada = EncargadaEntidad(nombreEncargada, nombreArea, formatoHora.format(fechaActual).toString())
                            obtenerCsvViewModel.ontenerDatosEncargada(encargada)
                            dialogo.dismiss()
                            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("No") { _, _ ->
                            // Manejar la cancelación o realizar otra acción específica
                        }
                        .create()
                    dialog.show()
                }
            }
        }
        dialogo.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Gracias por conceder el permiso.", Toast.LENGTH_SHORT).show()
                permissionsExplanationShown = true
            } else {
                if (!permissionsExplanationShown) {
                    showPermissionsExplanationDialog()
                } else if (permissionsExplanationShown && !obtenerCsvViewModel.hasStoragePermission(this)) {
                    Toast.makeText(this, "No se aceptaron los permisos.", Toast.LENGTH_SHORT).show()
                    permissionsExplanationShown = false
                }
            }
        }
    }

    private fun showPermissionsExplanationDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Permisos necesarios")
            .setMessage("La aplicación necesita acceso al almacenamiento para importar el archivo que contiene los Tickets.")
            .setPositiveButton("Intentar de nuevo") { _, _ ->
                permissionsExplanationShown = true
                obtenerCsvViewModel.permissionReadExternalStorage(this)
            }
            .setNegativeButton("Cancelar") { _, _ ->
                // Manejar la cancelación o realizar otra acción específica de tu aplicación
            }
            .create()
        dialog.show()
    }
}