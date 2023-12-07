package com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewSinConsumir

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carlasarai.TicketsKotlin.model.clasesModelo.ProductoTamanoModelo
import com.carlasarai.pruebanueva.R

class SinConsumirAdapter(private var platosSinConsumir: List<ProductoTamanoModelo>): RecyclerView.Adapter<SinConsumirViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinConsumirViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_platos, parent, false)
        return SinConsumirViewHolder(view)
    }

    override fun getItemCount() = platosSinConsumir.size

    override fun onBindViewHolder(holder: SinConsumirViewHolder, position: Int) {
        holder.render((platosSinConsumir[position]))
    }
}