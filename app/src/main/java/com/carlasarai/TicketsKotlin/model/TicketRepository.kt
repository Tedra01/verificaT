package com.carlasarai.TicketsKotlin.model

import androidx.lifecycle.LiveData
import com.carlasarai.TicketsKotlin.model.dao.TicketDao
import com.carlasarai.TicketsKotlin.model.entidades.AreaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketProductoCrossRef
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketWithProductos

class TicketRepository(private val ticketDao: TicketDao) {

    val getComensalesOfTicket: LiveData<List<TicketEntidad>> = ticketDao.getComensalesOfTicket(true)
    val getComensalesDisponiblesOfTicket: LiveData<List<TicketEntidad>> = ticketDao.getComensalesOfTicket(false)
    val getTicketProductoCrossRefConsumidos: LiveData<List<TicketProductoCrossRef>> = ticketDao.getTicketProductoCrossRefConsumidosSinConsumir(true)
    val getTicketProductoCrossRefSinConsumir: LiveData<List<TicketProductoCrossRef>> = ticketDao.getTicketProductoCrossRefConsumidosSinConsumir(false)
    val getTickets: LiveData<List<TicketEntidad>> = ticketDao.getTickets()
    val getProductos: LiveData<List<TicketProductoCrossRef>> = ticketDao.getProductos()
    val getProd: LiveData<List<ProductoEntidad>> = ticketDao.getProd()
    val getAreas: LiveData<List<AreaEntidad>> = ticketDao.getAreas()
    val getEncargadas: LiveData<List<EncargadaEntidad>> = ticketDao.getEncargada()
    val getAllProductsOfTickets: LiveData<List<TicketWithProductos>> = ticketDao.getAllProductosOfTicket()

    suspend fun addTicket(ticketEntidad: TicketEntidad){
        ticketDao.insertTicket(ticketEntidad)
    }
    suspend fun addProducto(productoEntidad: ProductoEntidad){
        ticketDao.insertProducto(productoEntidad)
    }
    suspend fun addArea(areaEntidad: AreaEntidad){
        ticketDao.insertArea(areaEntidad)
    }
    suspend fun addTicketProductoCrossRef(ticketProductoCrossRef: TicketProductoCrossRef){
        ticketDao.insertTicketProductoCrossRef(ticketProductoCrossRef)
    }
    suspend fun addEncargada(encargadaEntidad: EncargadaEntidad){
        ticketDao.insertEncargada(encargadaEntidad)
    }

    suspend fun updateTicket(ticketEntidad: TicketEntidad){
        ticketDao.updateTicket(ticketEntidad)
    }

    suspend fun updateTicketProductoCrossRef(ticketProductoCrossRef: TicketProductoCrossRef){
        ticketDao.updateTicketProductoCrossRef(ticketProductoCrossRef)
    }

    suspend fun deleteAllData() {
        ticketDao.deleteAllTicketTable()
        ticketDao.deleteAllProductoTable()
        ticketDao.deleteAllAreaTable()
        ticketDao.deleteAllTicketProductoCrossRefTable()
        ticketDao.deleteAllEncargadaTable()
    }

    fun getProductosOfTicket(numeroTicket: Int): TicketWithProductos? {
        val getProductosOfTick = ticketDao.getProductosOfTicket(numeroTicket)
        return getProductosOfTick?.takeIf { it.productos.isNotEmpty() }
    }

    fun getTicketProductoCrossRef(numeroTicket: Int): List<TicketProductoCrossRef> {
        return ticketDao.getTicketProductoCrossRef(numeroTicket)
    }

    fun getProductosOfTicketPorNombre(nombreComensal: String, areaNombre: String): List<TicketEntidad> {
        return ticketDao.getProductosOfTicketPorNombre(nombreComensal, areaNombre)
    }

}