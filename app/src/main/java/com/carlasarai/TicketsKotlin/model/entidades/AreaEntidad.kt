package com.carlasarai.TicketsKotlin.model.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "area_table")
data class AreaEntidad (
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "areaNombreComensal") val areaNombreComensal: String
)