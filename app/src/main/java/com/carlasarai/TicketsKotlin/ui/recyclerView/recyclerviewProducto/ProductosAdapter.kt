package com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewProducto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carlasarai.TicketsKotlin.model.entidades.ProductoEntidad
import com.carlasarai.pruebanueva.R

class ProductosAdapter(private var productos: List<ProductoEntidad>): RecyclerView.Adapter<ProductosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductosViewHolder(view)
    }

    override fun getItemCount() = productos.size

    override fun onBindViewHolder(holder: ProductosViewHolder, position: Int) {
        holder.render((productos[position]))
    }

}