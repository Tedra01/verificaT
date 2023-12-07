package com.carlasarai.TicketsKotlin.model.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "producto_table")
data class ProductoEntidad (
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "nombreProducto") val nombreProducto: String,
)
