package com.carlasarai.TicketsKotlin.ui.canjearComensal

import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlasarai.TicketsKotlin.model.entidades.TicketEntidad
import com.carlasarai.TicketsKotlin.model.entidades.relations.TicketProductoCrossRef
import com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewProducto.ProductosAdapter
import com.carlasarai.pruebanueva.R
import com.carlasarai.pruebanueva.databinding.FragmentCanjearComensalBinding



class CanjearComensalFragment : Fragment() {

    private var _binding: FragmentCanjearComensalBinding? = null
    private lateinit var productoAdapter: ProductosAdapter
    private val binding get() = _binding!!
    private lateinit var canjearComensalViewModel: CanjearComensalViewModel
    private lateinit var ticket: TicketEntidad
    //private lateinit var productos: List<ProductoEntidad>
    private lateinit var ticketProductoCrossRef: TicketProductoCrossRef
    var numeroTicketConfirmar: Int = -1
    private var comedorEncargada: String = ""
    private var fechaEncargada: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCanjearComensalBinding.inflate(inflater, container, false)
        val root: View = binding.root
        canjearComensalViewModel = ViewModelProvider(this).get(CanjearComensalViewModel::class.java)

        canjearComensalViewModel.encargada.observe(viewLifecycleOwner, Observer {
            comedorEncargada = ""
            fechaEncargada = ""
            if (it != null){
                for (i in it){
                    comedorEncargada = i.nombreArea
                    fechaEncargada = i.fecha
                }
            }
        })

        initUI()
        initListeners()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        canjearComensalViewModel.numero_codigo_qr.observe(viewLifecycleOwner, Observer {
            if (it != null){
                canjearComensalViewModel.devolverTicket(it.toInt())
                canjearComensalViewModel.devolverTicketProductoCrossRef(it.toInt())
            }
        })
    }

    private fun initListeners() {
        binding.buttonConfirmar.setOnClickListener {
            if(binding.pinTicket.text.isNotEmpty()){
                val dialog = AlertDialog.Builder(requireContext())
                    .setMessage("Confirmar Ticket")
                    .setPositiveButton("Confirmar") { _, _ ->
                        canjearComensalViewModel.updateTicket(ticket)
                        canjearComensalViewModel.devolverTicketProductoCrossRef(ticket.numeroTicket)
                        canjearComensalViewModel.getTicketProductoCrossRef.observe(viewLifecycleOwner, Observer {
                            for (i in it){
                                val confirmado = true
                                ticketProductoCrossRef = TicketProductoCrossRef(i.numeroTicket, i.nombreProducto, confirmado)
                                canjearComensalViewModel.updateTicketProductoCrossRef(ticketProductoCrossRef)
                                val currentFragment = requireActivity().supportFragmentManager.findFragmentById(
                                    R.id.drawer_layout)
                                if (currentFragment != null) {
                                    requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).commit()
                                }
                            }

                        })
                        Toast.makeText(requireContext(), "Confirmado Exitosamente", Toast.LENGTH_SHORT).show()
                        binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvProductos.adapter = null
                        binding.buscardor.setQuery("", false)
                        binding.pinTicket.text = ""
                        binding.textAreaComedor.text = ""
                        binding.textNombre.text = ""
                        binding.textAreaComensal.text = ""
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        // Manejar la cancelación o realizar otra acción específica de tu aplicación
                    }
                    .create()
                dialog.show()
            }
        }
        binding.buttonBorrar.setOnClickListener {
            binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
            binding.rvProductos.adapter = null
            binding.buscardor.setQuery("", false)
            binding.pinTicket.text = ""
            binding.textAreaComedor.text = ""
            binding.textNombre.text = ""
            binding.textAreaComensal.text = ""
            val currentFragment = requireActivity().supportFragmentManager.findFragmentById(
                R.id.drawer_layout)
            if (currentFragment != null) {
                requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).commit()
            }
        }
    }

    private fun initUI(){
        val args = arguments
        if (args != null && args.containsKey("numeroTicket")) {
            numeroTicketConfirmar = args.getInt("numeroTicket", -1)
            canjearComensalViewModel.devolverTicket(numeroTicketConfirmar)
            canjearComensalViewModel.devolverTicketProductoCrossRef(numeroTicketConfirmar)
            binding.buscardor.setQuery(numeroTicketConfirmar.toString(), false)

        }
        if (args != null && args.containsKey("numeroTicketQr")){
            numeroTicketConfirmar = args.getInt("numeroTicketQr", -1)
            canjearComensalViewModel.devolverTicket(numeroTicketConfirmar)
            canjearComensalViewModel.devolverTicketProductoCrossRef(numeroTicketConfirmar)
            binding.buscardor.setQuery(numeroTicketConfirmar.toString(), false)
        } else{
            binding.buscardor.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    canjearComensalViewModel.devolverTicket(query.toInt())
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            // Obtener el EditText interno del SearchView y aplicar el filtro
            val searchEditText = binding.buscardor.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            val maxLength = 4 // Cambié 4000 a 4 para limitar a 4 caracteres
            searchEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
        canjearComensalViewModel.getProductosOfTick.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.productos.isNotEmpty() && !it.ticketEntidad.confirmado && binding.buscardor.query.isNotBlank()) {
                    var confirmado = it.ticketEntidad.confirmado
                    confirmado = true
                    productoAdapter = ProductosAdapter(it.productos)
                    binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvProductos.adapter = productoAdapter
                    binding.pinTicket.text = it.ticketEntidad.pinConfirmacion.toString()
                    binding.textAreaComedor.text = it.ticketEntidad.areaNombreComedor
                    binding.textNombre.text = it.ticketEntidad.nombreComensal
                    binding.textAreaComensal.text = it.ticketEntidad.areaNombreComensal
                    ticket = TicketEntidad(it.ticketEntidad.numeroTicket, it.ticketEntidad.nombreComensal, it.ticketEntidad.areaNombreComensal, it.ticketEntidad.dia, it.ticketEntidad.nombreComensal, it.ticketEntidad.pinConfirmacion, confirmado)
                    //productos = it.productos
                    numeroTicketConfirmar = -1
                    if (comedorEncargada != it.ticketEntidad.areaNombreComedor && binding.buscardor.query.isNotBlank() && numeroTicketConfirmar == -1){
                        //showCustomToast("No pertenece a este comedor")
                        mostrarMensaje("El ticket no pertenece a este comedor")
                    }
                    if (fechaEncargada != it.ticketEntidad.dia && binding.buscardor.query.isNotBlank()){
                        //showCustomToast("El Ticket no es de hoy")
                        mostrarMensaje("El ticket no es de hoy")
                    }
                }
                if (it.ticketEntidad.confirmado && it.productos.isNotEmpty() && binding.buscardor.query.isNotBlank()) {
                    Toast.makeText(requireContext(), "Ya Fue Consumido", Toast.LENGTH_SHORT).show()
                    binding.buscardor.setQuery("", false)
                    val currentFragment = requireActivity().supportFragmentManager.findFragmentById(
                        R.id.drawer_layout)
                    if (currentFragment != null) {
                        requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).commit()
                    }
                }
            }
            if(it == null && binding.buscardor.query.isNotBlank()) {
                Toast.makeText(requireContext(), "No Existe", Toast.LENGTH_SHORT).show()
                binding.buscardor.setQuery("", false)
                val currentFragment = requireActivity().supportFragmentManager.findFragmentById(
                    R.id.drawer_layout)
                if (currentFragment != null) {
                    requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).commit()
                }
            } else {
                println("")
            }
        })
    }

    private fun showCustomToast(message: String) {
        val inflater = requireActivity().layoutInflater
        val layout = inflater.inflate(R.layout.toats, requireActivity().findViewById(R.id.custom_toast_container))

        val text = layout.findViewById<TextView>(R.id.textViewToast)
        text.text = message

        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view = layout.findViewById(R.id.custom_toast_container) // Asegúrate de que sea el ID correcto
        toast.show()
    }

    private fun mostrarMensaje(mensaje: String){
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ ->
            }
            .create()
        dialog.show()
    }
}

