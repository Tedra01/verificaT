package com.carlasarai.TicketsKotlin.ui.comensalesDisponibles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlasarai.TicketsKotlin.ui.recyclerView.recyclerviewComensal.ComensalAdapter
import com.carlasarai.pruebanueva.databinding.FragmentComensalesDisponiblesBinding

class ComensalesDisponiblesFragment : Fragment() {

    private var _binding: FragmentComensalesDisponiblesBinding? = null
    private val binding get() = _binding!!
    private lateinit var comensalAdapter: ComensalAdapter
    private lateinit var comensalesDisponiblesViewModel: ComensalesDisponiblesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentComensalesDisponiblesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        comensalesDisponiblesViewModel = ViewModelProvider(this).get(ComensalesDisponiblesViewModel::class.java)
        initUI()
        return root
    }

    private fun initUI(){

        comensalesDisponiblesViewModel.getComensalesDisponiblesOfTick.observe(viewLifecycleOwner, Observer {listaTicket ->
            if (listaTicket != null) {
                comensalAdapter = ComensalAdapter(listaTicket)
                binding.rvComensales.layoutManager = LinearLayoutManager(requireContext())
                binding.rvComensales.adapter = comensalAdapter
                binding.etDisponibles.text = listaTicket.size.toString()
            }
            binding.etFilter.addTextChangedListener {
                val ticketFilter = listaTicket.filter { ticket -> ticket.nombreComensal.lowercase().contains(it.toString().lowercase()) }
                comensalAdapter.updateTickets(ticketFilter)
            }
        })
        comensalesDisponiblesViewModel.getTicks.observe(viewLifecycleOwner, Observer {
            if (it != null){
                binding.etReservados.text = it.size.toString()
            }
        })
        comensalesDisponiblesViewModel.getEncargada.observe(viewLifecycleOwner, Observer {
            if(it != null){
                for (i in it){
                    binding.etFecha.text = i.fecha.toString()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}