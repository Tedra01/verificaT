package com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewProducto

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.pruebanueva.R

class ProductosViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvNombreProducto: AppCompatTextView = view.findViewById(R.id.tvNombreProducto)

    fun render(producto: ProductoEntidad){
        tvNombreProducto.text = producto.nombreProducto
    }

}