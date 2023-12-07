package com.carlasarai.TicketsKotlin.ui.canjearComensalPorNombreArea

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carlasarai.TicketsKotlin.model.TicketDataBase
import com.carlasarai.TicketsKotlin.model.TicketRepository
import com.carlasarai.TicketsKotlin.model.entidades.AreaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CanjearComensalPorNombreAreaViewModel(application: Application) : AndroidViewModel(application) {

    var getProductosOfTickPorNom: MutableLiveData<List<TicketEntidad>> = MutableLiveData()
    val getAreaas: LiveData<List<AreaEntidad>>
    private val repository: TicketRepository

    init {
        val ticketDao = TicketDataBase.getInstance(application).ticketDao
        repository = TicketRepository(ticketDao)
        getAreaas = repository.getAreas
    }
    fun devolverTicketPorNombre(nomComensal: String, areaNom: String){
        viewModelScope.launch(Dispatchers.IO){
            getProductosOfTickPorNom.postValue(repository.getProductosOfTicketPorNombre(nomComensal, areaNom))
        }
    }
}