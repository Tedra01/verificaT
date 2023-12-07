package com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewComensal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.pruebanueva.R

class ComensalAdapter(private var comensales: List<TicketEntidad>): RecyclerView.Adapter<ComensalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComensalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comensales, parent, false)
        return ComensalViewHolder(view)
    }

    override fun getItemCount() = comensales.size

    override fun onBindViewHolder(holder: ComensalViewHolder, position: Int) {
        holder.render((comensales[position]))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTickets(comensales: List<TicketEntidad>){
        this.comensales = comensales
        notifyDataSetChanged()
    }
}