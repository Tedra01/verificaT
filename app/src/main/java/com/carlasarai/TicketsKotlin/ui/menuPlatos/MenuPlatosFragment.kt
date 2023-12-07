package com.carlasarai.TicketsKotlin.ui.menuPlatos

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlasarai.TicketsKotlin.model.clasesModelo.ProductoTamanoModelo
import com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewConsumidos.ConsumidosAdapter
import com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewSinConsumir.SinConsumirAdapter
import com.carlasarai.pruebanueva.databinding.FragmentMenuPlatosBinding

class MenuPlatosFragment : Fragment() {

    private var _binding: FragmentMenuPlatosBinding? = null
    private val binding get() = _binding!!
    private lateinit var consumidosAdapter: ConsumidosAdapter
    private lateinit var sinConsumirAdapter: SinConsumirAdapter
    private lateinit var menuPlatosViewModel: MenuPlatosViewModel
    private val listaProductos: MutableList<String> = mutableListOf()
    private val listaProductosConsumidos: MutableList<ProductoTamanoModelo>  = mutableListOf()
    private val listaProductosSinConsumir: MutableList<ProductoTamanoModelo> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuPlatosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        menuPlatosViewModel = ViewModelProvider(this).get(MenuPlatosViewModel::class.java)
        initUI()
        return root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initUI(){
        menuPlatosViewModel.getEncargada.observe(viewLifecycleOwner, Observer {
            if(it != null){
                for (i in it){
                    binding.etFecha.text = i.fecha.toString()
                }
            }
        })
        menuPlatosViewModel.getProducto.observe(viewLifecycleOwner, Observer {listaProductoEntidad ->
            listaProductos.clear()
            if(listaProductoEntidad != null){
                for (i in listaProductoEntidad){
                    val nuevo = i.nombreProducto
                    listaProductos.add(nuevo)
                }
            }
        })

        menuPlatosViewModel.getPlatosConsumidos.observe(viewLifecycleOwner, Observer {listaticketProductoCrosRefConsumidos ->
            listaProductosConsumidos.clear()
            for (i in listaProductos) {
                if (listaticketProductoCrosRefConsumidos != null) {
                    binding.etConsumidos.text = listaticketProductoCrosRefConsumidos.size.toString()
                    val ticketsParaProducto = listaticketProductoCrosRefConsumidos.filter { it.nombreProducto == i }
                    val productoTamanoModelo = ProductoTamanoModelo(i, ticketsParaProducto.size)
                    listaProductosConsumidos.add(productoTamanoModelo)
                }
            }
            consumidosAdapter = ConsumidosAdapter(listaProductosConsumidos)
            binding.rvConsumidos.layoutManager = LinearLayoutManager(requireContext())
            binding.rvConsumidos.adapter = consumidosAdapter
            consumidosAdapter.notifyDataSetChanged()
        })

        menuPlatosViewModel.getPlatosSinConsumir.observe(viewLifecycleOwner, Observer { listaticketProductoCrosRefSinConsumir ->
            listaProductosSinConsumir.clear()
            for (i in listaProductos){
                if (listaticketProductoCrosRefSinConsumir != null){
                    binding.etSinConsumir.text = listaticketProductoCrosRefSinConsumir.size.toString()
                    val ticketsParaProducto = listaticketProductoCrosRefSinConsumir.filter { it.nombreProducto == i }
                    val productoTamanoModelo = ProductoTamanoModelo(i, ticketsParaProducto.size)
                    listaProductosSinConsumir.add(productoTamanoModelo)
                }
            }
            sinConsumirAdapter = SinConsumirAdapter(listaProductosSinConsumir)
            binding.rvSinConsumir.layoutManager = LinearLayoutManager(requireContext())
            binding.rvSinConsumir.adapter = sinConsumirAdapter
            sinConsumirAdapter.notifyDataSetChanged()
        })

        menuPlatosViewModel.getTotalProductos.observe(viewLifecycleOwner, Observer {
            if(it != null){
                binding.etTotalProductos.text = it.size.toString()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}