package com.carlasarai.TicketsKotlin.model.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "encargada_table")
data class EncargadaEntidad (
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "nombreEncargada") val nombreEncargada: String,
    @ColumnInfo(name = "nombreAreaComedor") val nombreArea: String,
    @ColumnInfo(name = "Fecha") val fecha: String
)