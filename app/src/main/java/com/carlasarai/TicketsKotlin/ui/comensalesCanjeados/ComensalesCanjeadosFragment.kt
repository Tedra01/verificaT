package com.carlasarai.TicketsKotlin.ui.comensalesCanjeados

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
import com.carlasarai.pruebanueva.databinding.FragmentComensalesCanjeadosBinding

class ComensalesCanjeadosFragment : Fragment() {

    private var _binding: FragmentComensalesCanjeadosBinding? = null
    private val binding get() = _binding!!
    private lateinit var comensalAdapter: ComensalAdapter
    private lateinit var comensalesCanjeadosViewModel: ComensalesCanjeadosViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentComensalesCanjeadosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        comensalesCanjeadosViewModel = ViewModelProvider(this).get(ComensalesCanjeadosViewModel::class.java)
        initUI()
        return root
    }

    private fun initUI(){

        comensalesCanjeadosViewModel.getComensalesOfTick.observe(viewLifecycleOwner, Observer {listaTicket ->
            if (listaTicket != null) {
                comensalAdapter = ComensalAdapter(listaTicket)
                binding.rvComensales.layoutManager = LinearLayoutManager(requireContext())
                binding.rvComensales.adapter = comensalAdapter
                binding.etCanjeados.text = listaTicket.size.toString()
            }
            binding.etFilter.addTextChangedListener {
                val ticketFilter = listaTicket.filter { ticket -> ticket.nombreComensal.lowercase().contains(it.toString().lowercase()) }
                comensalAdapter.updateTickets(ticketFilter)
            }
        })
        comensalesCanjeadosViewModel.getTicks.observe(viewLifecycleOwner, Observer {
            if (it != null){
                binding.etReservados.text = it.size.toString()

            }
        })
        comensalesCanjeadosViewModel.getEncargada.observe(viewLifecycleOwner, Observer {
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