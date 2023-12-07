package com.carlasarai.TicketsKotlin.ui.comensalesCanjeados

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.carlasarai.TicketsKotlin.model.TicketDataBase
import com.carlasarai.TicketsKotlin.model.TicketRepository
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad

class ComensalesCanjeadosViewModel(application: Application) : AndroidViewModel(application) {

    val getComensalesOfTick: LiveData<List<TicketEntidad>>
    val getTicks: LiveData<List<TicketEntidad>>
    val getEncargada: LiveData<List<EncargadaEntidad>>
    private val repository: TicketRepository

    init {
        val ticketDao = TicketDataBase.getInstance(application).ticketDao
        repository = TicketRepository(ticketDao)
        getComensalesOfTick = repository.getComensalesOfTicket
        getTicks = repository.getTickets
        getEncargada = repository.getEncargadas
    }
}