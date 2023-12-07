package com.carlasarai.TicketsKotlin.ui.canjearComensal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carlasarai.TicketsKotlin.model.TicketDataBase
import com.carlasarai.TicketsKotlin.model.TicketRepository
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketProductoCrossRef
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketWithProductos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CanjearComensalViewModel(application: Application) : AndroidViewModel(application) {

    var getProductosOfTick: MutableLiveData<TicketWithProductos> = MutableLiveData()
    var getTicketProductoCrossRef: MutableLiveData<List<TicketProductoCrossRef>> = MutableLiveData()
    private val repository: TicketRepository
    var numero_codigo_qr: MutableLiveData<String> = MutableLiveData()
    val encargada: LiveData<List<EncargadaEntidad>>

    init {
        val ticketDao = TicketDataBase.getInstance(application).ticketDao
        repository = TicketRepository(ticketDao)
        encargada = repository.getEncargadas
    }
    fun devolverTicket(id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            getProductosOfTick.postValue(repository.getProductosOfTicket(id))
        }
    }

    fun devolverTicketProductoCrossRef(id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            getTicketProductoCrossRef.postValue(repository.getTicketProductoCrossRef(id))
        }
    }

    fun updateTicket(ticketEntidad: TicketEntidad){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTicket(ticketEntidad)
        }
    }

    fun updateTicketProductoCrossRef(ticketProductoCrossRef: TicketProductoCrossRef){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTicketProductoCrossRef(ticketProductoCrossRef)
        }
    }
}
