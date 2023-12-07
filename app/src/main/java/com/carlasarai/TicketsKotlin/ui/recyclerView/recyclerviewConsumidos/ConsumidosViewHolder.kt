package com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewConsumidos

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.carlasarai.TicketsKotlin.model.clasesModelo.ProductoTamanoModelo
import com.carlasarai.pruebanueva.R

class ConsumidosViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvNombrePlato: AppCompatTextView = view.findViewById(R.id.tv_nombre_plato)
    val tvTotalPlato: AppCompatTextView = view.findViewById(R.id.tv_total_plato)

    fun render(productos: ProductoTamanoModelo){
        tvNombrePlato.text = productos.nombre
        tvTotalPlato.text = productos.totalProducto.toString()
    }
}