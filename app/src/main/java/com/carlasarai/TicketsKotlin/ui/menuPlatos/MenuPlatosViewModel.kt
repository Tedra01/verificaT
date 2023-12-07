package com.carlasarai.TicketsKotlin.ui.menuPlatos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.carlasarai.TicketsKotlin.model.TicketDataBase
import com.carlasarai.TicketsKotlin.model.TicketRepository
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketProductoCrossRef

class MenuPlatosViewModel(application: Application) : AndroidViewModel(application) {

    val getPlatosSinConsumir: LiveData<List<TicketProductoCrossRef>>
    val getPlatosConsumidos: LiveData<List<TicketProductoCrossRef>>
    val getTotalProductos: LiveData<List<TicketProductoCrossRef>>
    val getProducto:LiveData<List<ProductoEntidad>>
    val getEncargada: LiveData<List<EncargadaEntidad>>
    private val repository: TicketRepository

    init {
        val ticketDao = TicketDataBase.getInstance(application).ticketDao
        repository = TicketRepository(ticketDao)
        getTotalProductos = repository.getProductos
        getProducto = repository.getProd
        getPlatosConsumidos = repository.getTicketProductoCrossRefConsumidos
        getPlatosSinConsumir = repository.getTicketProductoCrossRefSinConsumir
        getEncargada = repository.getEncargadas
    }
}