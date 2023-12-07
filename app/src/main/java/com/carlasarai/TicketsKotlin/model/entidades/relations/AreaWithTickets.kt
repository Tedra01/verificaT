package com.carlasarai.TicketsKotlin.model.entidades.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.carlasarai.TicketsKotlin.model.entidades.AreaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad

data class AreaWithTickets (
    @Embedded val areaEntidad: AreaEntidad,
    @Relation(
        parentColumn = "areaNombreComensal",
        entityColumn = "areaNombreComensal"
    )
    val tickets: List<TicketEntidad>
)