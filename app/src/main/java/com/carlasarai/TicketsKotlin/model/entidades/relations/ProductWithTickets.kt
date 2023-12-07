package com.carlasarai.TicketsKotlin.model.entidades.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad

data class ProductWithTickets (
    @Embedded val productoEntidad: ProductoEntidad,
    @Relation(
        parentColumn = "nombreProducto",
        entityColumn = "numeroTicket",
        associateBy = Junction(TicketProductoCrossRef::class)
    )
    val tickets: List<TicketEntidad>
    )