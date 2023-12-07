package com.carlasarai.TicketsKotlin.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carlasarai.TicketsKotlin.model.dao.TicketDao
import com.carlasarai.TicketsKotlin.model.entidades.AreaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.EncargadaEntidad
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketProductoCrossRef

@Database(
    entities = [
        TicketEntidad::class,
        ProductoEntidad::class,
        AreaEntidad::class,
        TicketProductoCrossRef::class,
        EncargadaEntidad::class],
    version = 1)

abstract class TicketDataBase : RoomDatabase() {
    abstract val ticketDao: TicketDao

    companion object{
        @Volatile
        private var INSTANCE: TicketDataBase? = null

        fun getInstance(context: Context): TicketDataBase {
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TicketDataBase::class.java,
                    "ticket_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}