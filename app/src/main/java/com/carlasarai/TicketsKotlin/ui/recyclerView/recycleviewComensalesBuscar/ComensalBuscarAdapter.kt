package com.carlasarai.TicketsKotlin.ui.recyclerView.recycleviewComensalesBuscar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.pruebanueva.R

class ComensalBuscarAdapter(private var comensales: List<TicketEntidad>): RecyclerView.Adapter<ComensalBuscarViewHolder>() {

    var id: MutableLiveData<Int> = MutableLiveData()
    lateinit var view: View
    var selectedPosition: Int = RecyclerView.NO_POSITION


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComensalBuscarViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_comensales_buscar, parent, false)
        return ComensalBuscarViewHolder(view)
    }

    override fun getItemCount() = comensales.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ComensalBuscarViewHolder, position: Int) {
        holder.render((comensales[position]))

        holder.cardViewComensalBuscar.setBackgroundResource(
            if (position == selectedPosition) R.color.color_letra_opaco else R.color.fond_claro
        )

        holder.cardViewComensalBuscar.setOnClickListener {
            System.out.println("Ingreso Al card")
            id.postValue(comensales[position].numeroTicket)
            selectedPosition = position
            notifyDataSetChanged()
        }
    }
}