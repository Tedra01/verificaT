package com.carlasarai.TicketsKotlin.model.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "ticket_table")
data class TicketEntidad (
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "numeroTicket") val numeroTicket: Int,
    @ColumnInfo(name = "areaNombreComensal") val areaNombreComensal: String,
    @ColumnInfo(name = "areaNombreComedor") val areaNombreComedor: String,
    @ColumnInfo(name = "dia") val dia: String,
    @ColumnInfo(name = "nombreComensal") val nombreComensal: String,
    @ColumnInfo(name = "pinConfirmacion") val pinConfirmacion: Int,
    @ColumnInfo(name = "confirmado") var confirmado: Boolean
    )