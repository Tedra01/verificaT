package com.carlasarai.TicketsKotlin.ui.recyclerView.recycleviewComensalesBuscar

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.pruebanueva.R

class ComensalBuscarViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val cardViewComensalBuscar: CardView = view.findViewById(R.id.cardViewComensalBuscar)
    val tvNombreComensalBuscar: AppCompatTextView = view.findViewById(R.id.tvNombreComensalBuscar)
    val tvNumeroTicketBuscar: AppCompatTextView = view.findViewById(R.id.tv_numeroTicketBuscar)

    fun render(comensal: TicketEntidad){
        tvNumeroTicketBuscar.text = comensal.numeroTicket.toString()
        tvNombreComensalBuscar.text = comensal.nombreComensal
    }
}