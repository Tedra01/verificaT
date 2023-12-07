package com.carlasarai.TicketsKotlin.ui.canjearComensalPorNombreArea

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlasarai.TicketsKotlin.core.hideKeyBoard
import com.carlasarai.TicketsKotlin.ui.canjearComensal.CanjearComensalFragment
import com.carlasarai.TicketsKotlin.ui.recyclerView.recycleviewComensalesBuscar.ComensalBuscarAdapter
import com.carlasarai.pruebanueva.R
import com.carlasarai.pruebanueva.databinding.FragmentCanjearNombreAreaBinding

class CanjearComensalPorNombreArea : Fragment() {

    private var _binding: FragmentCanjearNombreAreaBinding? = null
    private lateinit var comensalBuscarAdapter: ComensalBuscarAdapter
    private val binding get() = _binding!!
    private lateinit var canjearComensalPorNombreAreaViewModel: CanjearComensalPorNombreAreaViewModel
    private var areas: MutableList<String> = mutableListOf()
    private var numeroTicket: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCanjearNombreAreaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        canjearComensalPorNombreAreaViewModel = ViewModelProvider(this).get(CanjearComensalPorNombreAreaViewModel::class.java)
        initUI()
        initListeners()
        val autoCompleteTextView: AutoCompleteTextView = binding.autoCompliteTextView
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item, areas)
        autoCompleteTextView.setAdapter(arrayAdapter)
        return root
    }

    private fun initListeners() {
        binding.bottonNombreArea.setOnClickListener {
            it.hideKeyBoard()
            val newNombre = binding.textNombre.text.toString()
            val newArea = binding.autoCompliteTextView.text.toString()
            buscarPorNombre(newNombre, newArea)
            binding.textNombre.text = null
            binding.autoCompliteTextView.text = null
        }
        binding.bottonIrAlTicket.setOnClickListener {
            if (numeroTicket != -1){
                val bundle = Bundle()
                bundle.putInt("numeroTicket", numeroTicket)
                val canjearComensalFragment = CanjearComensalFragment()
                canjearComensalFragment.arguments = bundle
                numeroTicket = -1
                // Reemplazar el fragmento actual con CanjearComensalFragment
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.drawer_layout, canjearComensalFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                binding.rvComensales.layoutManager = LinearLayoutManager(requireContext())
                binding.rvComensales.adapter = null
            }else{
                Toast.makeText(requireContext(), "Escoje un Comensal", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun initUI(){
        canjearComensalPorNombreAreaViewModel.getAreaas.observe(viewLifecycleOwner, Observer {
            areas.clear()
            if (it != null){
                for (i in it){
                    areas.add(i.areaNombreComensal)
                }
            }
        })
        canjearComensalPorNombreAreaViewModel.getProductosOfTickPorNom.observe(viewLifecycleOwner, Observer { listaTicketEntidad ->
            if (listaTicketEntidad != null) {
                comensalBuscarAdapter = ComensalBuscarAdapter(listaTicketEntidad)
                binding.rvComensales.layoutManager = LinearLayoutManager(requireContext())
                binding.rvComensales.adapter = comensalBuscarAdapter
                comensalBuscarAdapter.id.observe(viewLifecycleOwner, Observer {
                    if (it != null){
                        numeroTicket = it
                    }
                })
            }else{
                Toast.makeText(requireContext(), "El comensal no existe", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun buscarPorNombre(nombre: String, area: String) {
        val newNombre = nombre.isBlank()
        val newArea = area.isBlank()
        if (!newNombre && !newArea) {
            canjearComensalPorNombreAreaViewModel.devolverTicketPorNombre(nombre, area)
        } else {
            Toast.makeText(requireContext(), "Tienes que poner el nombre del comensal y el nombre del area", Toast.LENGTH_LONG).show()
        }
    }
}