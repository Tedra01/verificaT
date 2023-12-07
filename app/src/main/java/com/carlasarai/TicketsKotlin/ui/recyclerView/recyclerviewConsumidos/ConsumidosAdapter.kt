package com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewConsumidos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carlasarai.TicketsKotlin.model.clasesModelo.ProductoTamanoModelo
import com.carlasarai.pruebanueva.R

class ConsumidosAdapter(private var platosConsumidos: List<ProductoTamanoModelo>): RecyclerView.Adapter<ConsumidosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumidosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_platos, parent, false)
        return ConsumidosViewHolder(view)
    }

    override fun getItemCount() = platosConsumidos.size

    override fun onBindViewHolder(holder: ConsumidosViewHolder, position: Int) {
        holder.render((platosConsumidos[position]))
    }
}