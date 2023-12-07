package com.carlasarai.TicketsKotlin.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.carlasarai.TicketsKotlin.model.entidades.AreaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketProductoCrossRef
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketWithProductos

@Dao
interface TicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticketEntidad: TicketEntidad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducto(productoEntidad: ProductoEntidad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArea(areaEntidad: AreaEntidad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicketProductoCrossRef(crossRef: TicketProductoCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEncargada(encargadaEntidad: EncargadaEntidad)

    @Update
    suspend fun updateTicket(ticketEntidad: TicketEntidad)

    @Update
    suspend fun updateTicketProductoCrossRef(ticketProductoCrossRef: TicketProductoCrossRef)

    @Query("DELETE FROM ticket_table")
    suspend fun deleteAllTicketTable()

    @Query("DELETE FROM producto_table")
    suspend fun deleteAllProductoTable()

    @Query("DELETE FROM area_table")
    suspend fun deleteAllAreaTable()

    @Query("DELETE FROM producto_ticket_table")
    suspend fun deleteAllTicketProductoCrossRefTable()

    @Query("DELETE FROM encargada_table")
    suspend fun deleteAllEncargadaTable()

    @Transaction
    @Query("SELECT * FROM ticket_table WHERE numeroTicket = :numeroTicket")
    fun getProductosOfTicket(numeroTicket: Int): TicketWithProductos?

    @Transaction
    @Query("SELECT * FROM producto_ticket_table WHERE numeroTicket = :numeroTicket")
    fun getTicketProductoCrossRef(numeroTicket: Int): List<TicketProductoCrossRef>

    @Transaction
    @Query("SELECT * FROM ticket_table WHERE nombreComensal = :nombreComensal AND areaNombreComensal = :areaNombreComensal")
    fun getProductosOfTicketPorNombre(nombreComensal: String, areaNombreComensal: String): List<TicketEntidad>

    @Transaction
    @Query("SELECT * FROM ticket_table WHERE confirmado = :confirmado")
    fun getComensalesOfTicket(confirmado: Boolean): LiveData<List<TicketEntidad>>

    @Transaction
    @Query("SELECT * FROM producto_ticket_table WHERE confirmado = :confirmado")
    fun getTicketProductoCrossRefConsumidosSinConsumir(confirmado: Boolean): LiveData<List<TicketProductoCrossRef>>

    @Transaction
    @Query("SELECT * FROM ticket_table")
    fun getTickets(): LiveData<List<TicketEntidad>>

    @Transaction
    @Query("SELECT * FROM producto_table")
    fun getProd(): LiveData<List<ProductoEntidad>>

    @Transaction
    @Query("SELECT * FROM producto_ticket_table")
    fun getProductos(): LiveData<List<TicketProductoCrossRef>>

    @Transaction
    @Query("SELECT * FROM encargada_table")
    fun getEncargada(): LiveData<List<EncargadaEntidad>>

    @Transaction
    @Query("SELECT * FROM area_table")
    fun getAreas(): LiveData<List<AreaEntidad>>

    @Transaction
    @Query("SELECT * FROM ticket_table")
    fun getAllProductosOfTicket(): LiveData<List<TicketWithProductos>>
}