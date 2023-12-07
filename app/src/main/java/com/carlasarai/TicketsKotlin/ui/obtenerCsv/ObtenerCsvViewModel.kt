package com.carlasarai.TicketsKotlin.ui.obtenerCsv

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carlasarai.TicketsKotlin.model.TicketDataBase
import com.carlasarai.TicketsKotlin.model.TicketRepository
import com.carlasarai.TicketsKotlin.model.entidades.AreaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketProductoCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class ObtenerCsvViewModel(application: Application): AndroidViewModel(application) {

    val isLoading = MutableLiveData(false)
    val overlayView = MutableLiveData(false)
    //val buttonImportar = MutableLiveData(R.color.fondo_claro_oscuro)
    //val buttonDescargar = MutableLiveData(R.color.fondo_claro_oscuro)
    val encargada: LiveData<List<EncargadaEntidad>>
    val getTicks: LiveData<List<TicketEntidad>>
    private val repository: TicketRepository

    init {
        val ticketDao = TicketDataBase.getInstance(application).ticketDao
        repository = TicketRepository(ticketDao)
        getTicks = repository.getTickets
        encargada = repository.getEncargadas
    }
    // Constants
    private val PERMISSION_REQUEST_CODE = 123

    fun permissionReadExternalStorage(activity: Activity){
        if (!hasStoragePermission(activity)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    fun hasStoragePermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun ontenerDatosEncargada(encargadaEntidad: EncargadaEntidad){
        viewModelScope.launch(Dispatchers.IO){
            repository.addEncargada(encargadaEntidad)
        }
    }

    fun expotarArchivo(inputStream:InputStream, context: Context) {

        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        if (!bufferedReader.equals("text/*")){
            // Leer la primera línea para omitir las cabeceras del CSV
            val linea = bufferedReader.readLine()
            if (!linea.toString().isBlank()){
                if (linea.split(",").size == 7){
                    isLoading.postValue(true)
                    overlayView.postValue(true)
                    //buttonImportar.postValue(R.color.color_letra_opaco)
                    //buttonDescargar.postValue(R.color.color_letra_opaco)
                    try {
                        //bufferedReader.readLine()
                        // Leer cada línea del CSV
                        while (bufferedReader.readLine().also { line = it } != null) {
                            // Dividir la línea en partes usando la coma como delimitador
                            val parts = line!!.split(",")
                            // Obtener los valores individuales
                            val numeroTicket = parts[0].toInt()
                            val areaNombreComensal = parts[1]
                            val areaNombreComedor = parts[2]
                            val fecha = parts[3]
                            val nombreComensal = parts[4]
                            val pinConfirmacion = parts[5].toInt()
                            // Insertar en TicketEntidad
                            val ticketEntidad = TicketEntidad(
                                numeroTicket,
                                areaNombreComensal,
                                areaNombreComedor,
                                fecha,
                                nombreComensal,
                                pinConfirmacion,
                                confirmado = false
                            )
                            //tickets.add(ticketEntidad)
                            addTicket(ticketEntidad)
                            // Insertar en AreaEntidad
                            val areaEntidad = AreaEntidad(areaNombreComensal)
                            //area.add(areaEntidad)
                            addArea(areaEntidad)
                            // Obtener la lista de productos separados por ";"
                            val productoslista = parts[6].split(";")
                            for(prod in productoslista) {
                                // Insertar en ProductoEntidad
                                val productoEntidad = ProductoEntidad(prod)
                                //productos.add(productoEntidad)
                                addProducto(productoEntidad)
                                // Insertar en TicketProductoCrossRef
                                val crossRef = TicketProductoCrossRef(numeroTicket, prod, confirmado = false)
                                //ticketProductoCrossRef.add(crossRef)
                                addTicketProductoCrossRef(crossRef)
                            }
                        }
                    }catch (e: IOException) {
                        // Manejar excepciones de lectura de archivo
                        e.printStackTrace()
                    } finally {
                        try {
                            // Cerrar el BufferedReader al finalizar
                            bufferedReader.close()
                        } catch (e: IOException) {
                            // Manejar excepciones al cerrar el archivo
                            e.printStackTrace()
                        }
                    }
                }else {
                    Toast.makeText(context, "El archivo es incorrecto", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(context, "El archivo está vacio", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(context, "El archivo es incorrecto", Toast.LENGTH_LONG).show()
        }
    }

    fun addTicket(ticketEntidad: TicketEntidad){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTicket(ticketEntidad)
        }
    }
    fun addProducto(productoEntidad: ProductoEntidad){
        viewModelScope.launch(Dispatchers.IO){
            repository.addProducto(productoEntidad)
        }
    }
    fun addArea(areaEntidad: AreaEntidad){
        viewModelScope.launch(Dispatchers.IO){
            repository.addArea(areaEntidad)
        }
    }
    fun addTicketProductoCrossRef(ticketProductoCrossRef: TicketProductoCrossRef){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTicketProductoCrossRef(ticketProductoCrossRef)
        }
    }

     fun onSeleccionArchivo(uri: Uri, context: Context) {
         // Pasar el InputStream a la lógica de importación
         //this.context = context
         val inputStream = context.contentResolver.openInputStream(uri)
         if (inputStream != null) {
             expotarArchivo(inputStream, context)
         }
     }
}