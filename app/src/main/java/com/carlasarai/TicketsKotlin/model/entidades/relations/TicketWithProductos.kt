package com.carlasarai.TicketsKotlin.model.entidades.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad

data class TicketWithProductos(
    @Embedded val ticketEntidad: TicketEntidad,
    @Relation(
        parentColumn = "numeroTicket",
        entityColumn = "nombreProducto",
        associateBy = Junction(TicketProductoCrossRef::class)
    )
    val productos: List<ProductoEntidad>
)