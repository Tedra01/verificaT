package com.carlasarai.TicketsKotlin.ui.comensalesDisponibles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.carlasarai.TicketsKotlin.model.TicketDataBase
import com.carlasarai.TicketsKotlin.model.TicketRepository
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad

class ComensalesDisponiblesViewModel(application: Application) : AndroidViewModel(application) {

    val getComensalesDisponiblesOfTick: LiveData<List<TicketEntidad>>
    val getTicks: LiveData<List<TicketEntidad>>
    val getEncargada: LiveData<List<EncargadaEntidad>>
    private val repository: TicketRepository

    init {
        val ticketDao = TicketDataBase.getInstance(application).ticketDao
        repository = TicketRepository(ticketDao)
        getComensalesDisponiblesOfTick = repository.getComensalesDisponiblesOfTicket
        getTicks = repository.getTickets
        getEncargada = repository.getEncargadas
    }
}