package com.example.kotlin.examenmoviles.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examenmoviles.R
import com.example.kotlin.examenmoviles.framework.adapter.EventAdapter
import com.example.kotlin.examenmoviles.framework.viewmodel.ExamenViewModel

class ExamenFragment : Fragment() {

    private lateinit var viewModel: ExamenViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_examen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        eventAdapter = EventAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = eventAdapter

        // Configuración del ViewModel
        viewModel = ViewModelProvider(requireActivity()).get(ExamenViewModel::class.java)

        // Observa los eventos históricos
        viewModel.eventos.observe(viewLifecycleOwner) { eventos ->
            eventAdapter.updateData(eventos)
        }

        // Observa los errores
        viewModel.error.observe(viewLifecycleOwner) { mensajeError ->
            Toast.makeText(requireContext(), mensajeError, Toast.LENGTH_LONG).show()
        }

        // Solicitar eventos históricos (página 1)
        viewModel.fetchEvents(page = 1)
    }
}
