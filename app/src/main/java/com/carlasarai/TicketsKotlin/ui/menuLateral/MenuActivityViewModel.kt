package com.carlasarai.TicketsKotlin.ui.menuLateral

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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.carlasarai.TicketsKotlin.model.TicketDataBase
import com.carlasarai.TicketsKotlin.model.TicketRepository
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketProductoCrossRef
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketWithProductos
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

class MenuActivityViewModel(application: Application): AndroidViewModel(application) {

    val isLoading = MutableLiveData(false)
    val overlayView = MutableLiveData(false)
    private val repository: TicketRepository
    var getAllProductOfTickets: LiveData<List<TicketWithProductos>>
    val getPlatosSinConsumir: LiveData<List<TicketProductoCrossRef>>
    val getPlatosConsumidos: LiveData<List<TicketProductoCrossRef>>
    val encargada: LiveData<List<EncargadaEntidad>>
    //ticketCanjeado
    val getComensalesOfTick: LiveData<List<TicketEntidad>>
    //ticketDisponibles
    val getComensalesDisponiblesOfTick: LiveData<List<TicketEntidad>>
    //totalTicket
    val getTicks: LiveData<List<TicketEntidad>>
    //totalPlatos
    val getTotalProductos: LiveData<List<TicketProductoCrossRef>>
    //cantidadProductos
    val getProducto:LiveData<List<ProductoEntidad>>
    private val PERMISSION_REQUEST_CODE = 123
    private val listaInformacionLineaUno: MutableList<String>  = mutableListOf()
    private val listaInformacionLineaDos: MutableList<String>  = mutableListOf("", "", "", "", "", "", "", "", "")
    private val listaProductos: MutableList<String> = mutableListOf()
    var mantenerLista: MutableLiveData<Int> = MutableLiveData(0)


    init {
        val ticketDao = TicketDataBase.getInstance(application).ticketDao
        repository = TicketRepository(ticketDao)
        getAllProductOfTickets = repository.getAllProductsOfTickets
        getPlatosConsumidos = repository.getTicketProductoCrossRefConsumidos
        getPlatosSinConsumir = repository.getTicketProductoCrossRefSinConsumir
        encargada = repository.getEncargadas
        getComensalesOfTick = repository.getComensalesOfTicket
        getComensalesDisponiblesOfTick = repository.getComensalesDisponiblesOfTicket
        getTicks = repository.getTickets
        getTotalProductos = repository.getProductos
        getProducto = repository.getProd
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllData()
        }
    }

    fun initScanner(): ScanOptions{
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Escanea el código QR")
            setCameraId(0)
            setBeepEnabled(false)
            setTorchEnabled(false)
        }
        return options
    }

    fun permissionReadExternalStorage(activity: Activity){
        if (!hasStoragePermission(activity)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    fun hasStoragePermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun onSeleccionArchivo(uri: Uri, context: Context, lifecycle: LifecycleOwner) {
        exportarArchivo(uri, context, lifecycle)
    }

    fun exportarArchivo(uri: Uri, context: Context, lifecycle: LifecycleOwner) {
        isLoading.postValue(true)
        overlayView.postValue(true)
        getAllProductOfTickets.observe(lifecycle, Observer { listaTicketProductos ->
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val writer = BufferedWriter(OutputStreamWriter(outputStream))
                    // Agrega las cabeceras si es necesario
                    val headerRecord: CharSequence = "NumeroTicket, NombreAreaComensal, NombreAreaComedor, Fecha, NombreComensal, PinConfirmacion, Confirmado, Productos"
                    headerRecord.split(",")
                    writer.append(headerRecord)
                    writer.append("\n")
                    // Agrega tus datos desde la base de datos
                    for (ticket in listaTicketProductos) {
                        val numeroTicket = ticket.ticketEntidad.numeroTicket.toString()
                        val nombreAreaComensal = ticket.ticketEntidad.areaNombreComensal.toString()
                        val nombreAreaComedor = ticket.ticketEntidad.areaNombreComedor.toString()
                        val fecha = ticket.ticketEntidad.dia.toString()
                        val nombreComensal = ticket.ticketEntidad.nombreComensal.toString()
                        val pinConfirmacion = ticket.ticketEntidad.pinConfirmacion.toString()
                        val confirmado = ticket.ticketEntidad.confirmado.toString()
                        var productos: MutableList<String> = mutableListOf()
                        for (prod in ticket.productos){
                            productos.add(prod.nombreProducto)
                        }
                        productos.toString()
                        val row: CharSequence = "$numeroTicket, $nombreAreaComensal, $nombreAreaComedor, $fecha, $nombreComensal, $pinConfirmacion, $confirmado, ${productos}"
                        writer.append(row)
                        writer.append("\n")
                        productos = mutableListOf()
                    }
                    writer.close()
                }
            }catch (e: IOException) {
                e.printStackTrace()
                // Manejar la excepción según tus necesidades
                Toast.makeText(context, "Error al crear el archivo", Toast.LENGTH_SHORT).show()
            }

        })
        isLoading.postValue(false)
        overlayView.postValue(false)
        Toast.makeText(context, "Archivo creado exitosamente", Toast.LENGTH_SHORT).show()
    }

    fun onSeleccionArchivoInformacion(uri: Uri, context: Context) {
        exportarArchivoInformacion(uri, context)
    }

    fun exportarArchivoInformacion(uri: Uri, context: Context) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                val writer = BufferedWriter(OutputStreamWriter(outputStream))
                // Agrega las cabeceras si es necesario
                val charSequenceUno: CharSequence = listaInformacionLineaUno.joinToString(", ")
                writer.append(charSequenceUno)
                writer.append("\n")
                val charSequenceDos: CharSequence = listaInformacionLineaDos.joinToString(", ")
                println(charSequenceDos.toString())
                println(charSequenceUno.toString())
                writer.append(charSequenceDos)
                writer.close()
            }
        }catch (e: IOException) {
            e.printStackTrace()
            // Manejar la excepción según tus necesidades
            Toast.makeText(context, "Error al crear el archivo", Toast.LENGTH_SHORT).show()
        }
    }

    fun llenarListas(lifecycle: LifecycleOwner){
        listaInformacionLineaUno.clear()
        val headerRecord: List<String> = listOf("Encargada", "Comedor", "Fecha", "TotalTickets", "TotalTicketsConfirmados", "TotalTicketsSinConsumir", "TotalPlatosConsumidos", "TotalPlatosSinConsumir", "TotalPlatos")
        listaInformacionLineaUno.addAll(headerRecord)
        getProducto.observe(lifecycle, Observer {listaProductoEntidad ->
            listaProductos.clear()
            listaProductoEntidad.forEach { prod ->
                listaInformacionLineaUno.add(prod.nombreProducto)
                listaProductos.add(prod.nombreProducto)
            }
        })
        getTotalProductos.observe(lifecycle, Observer {listaticketProductoCrosRef ->
            listaInformacionLineaDos[8] = listaticketProductoCrosRef.size.toString()
            var cont = 9
            for (prod in listaProductos) {
                println(prod)
                if (listaticketProductoCrosRef != null) {
                    val ticketsParaProducto = listaticketProductoCrosRef.filter { it.nombreProducto == prod }
                    if (mantenerLista.value == 0){
                        listaInformacionLineaDos.add(cont, ticketsParaProducto.size.toString())
                        cont += 1
                        mantenerLista.postValue(1)
                    }
                }
            }
        })
        encargada.observe(lifecycle, Observer {listaEncargada ->
            for (i in listaEncargada){
                listaInformacionLineaDos[0] = i.nombreEncargada
                listaInformacionLineaDos[1] = i.nombreArea
                listaInformacionLineaDos[2] = i.fecha
            }
        })
        getTicks.observe(lifecycle, Observer {getTicks ->
            listaInformacionLineaDos[3] = getTicks.size.toString()
        })
        getComensalesOfTick.observe(lifecycle, Observer {getComensalesOfTick ->
            listaInformacionLineaDos[4] = getComensalesOfTick.size.toString()
        })
        getComensalesDisponiblesOfTick.observe(lifecycle, Observer {getComensalesDisponiblesOfTick ->
            listaInformacionLineaDos[5] = getComensalesDisponiblesOfTick.size.toString()
        })
        getPlatosConsumidos.observe(lifecycle, Observer { getPlatosConsumidos ->
            listaInformacionLineaDos[6] = getPlatosConsumidos.size.toString()
        })
        getPlatosSinConsumir.observe(lifecycle, Observer { getPlatosSinConsumir ->
            listaInformacionLineaDos[7] = getPlatosSinConsumir.size.toString()
        })
    }
}