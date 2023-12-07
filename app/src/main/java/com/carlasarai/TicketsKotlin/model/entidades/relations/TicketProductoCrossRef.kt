package com.carlasarai.TicketsKotlin.model.entidades.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["numeroTicket", "nombreProducto"], tableName = "producto_ticket_table")
data class TicketProductoCrossRef(
    val numeroTicket: Int,
    val nombreProducto: String,
    @ColumnInfo(name = "confirmado") var confirmado: Boolean
)