package com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewComensal

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.pruebanueva.R

class ComensalViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvNombreComensal: AppCompatTextView = view.findViewById(R.id.tvNombreComensal)
    private val tvNumeroTicket: AppCompatTextView = view.findViewById(R.id.tv_numeroTicket)

    fun render(comensal: TicketEntidad){
        tvNumeroTicket.text = comensal.numeroTicket.toString()
        tvNombreComensal.text = comensal.nombreComensal
    }

}