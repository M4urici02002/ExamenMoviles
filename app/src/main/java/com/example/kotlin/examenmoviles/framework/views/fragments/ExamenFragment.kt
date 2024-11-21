package com.example.kotlin.examenmoviles.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examenmoviles.R
import com.example.kotlin.examenmoviles.framework.adapter.EventAdapter
import com.example.kotlin.examenmoviles.framework.viewmodel.ExamenViewModel

/**
 * Fragmento principal para mostrar y filtrar eventos históricos.
 */
class ExamenFragment : Fragment() {

    private lateinit var viewModel: ExamenViewModel
    private lateinit var eventAdapter: EventAdapter
    private lateinit var placeSpinner: Spinner
    private lateinit var dateRangeSpinner: Spinner
    private lateinit var applyFilterButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_examen, container, false)
    }

    /**
     * Configura la vista del fragmento después de haber sido creada.
     *
     * @param view La vista principal del fragmento.
     * @param savedInstanceState Estado guardado anteriormente del fragmento.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        eventAdapter = EventAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = eventAdapter

        placeSpinner = view.findViewById(R.id.placeSpinner)
        dateRangeSpinner = view.findViewById(R.id.dateRangeSpinner)
        applyFilterButton = view.findViewById(R.id.applyFilterButton)

        viewModel = ViewModelProvider(requireActivity()).get(ExamenViewModel::class.java)

        // Observa los datos originales
        viewModel.eventos.observe(viewLifecycleOwner) { eventos ->
            eventAdapter.updateData(eventos)
        }

        // Observa los datos filtrados
        viewModel.filteredEvents.observe(viewLifecycleOwner) { filtered ->
            eventAdapter.updateData(filtered)
        }

        // Observa los mensajes de error
        viewModel.error.observe(viewLifecycleOwner) { mensajeError ->
            Toast.makeText(requireContext(), mensajeError, Toast.LENGTH_LONG).show()
        }

        // Configuración del botón para aplicar filtros
        applyFilterButton.setOnClickListener {
            val selectedPlace = placeSpinner.selectedItem.toString()
            val selectedRange = dateRangeSpinner.selectedItem.toString()
            viewModel.filterEvents(
                if (selectedPlace == "Todos") null else selectedPlace,
                selectedRange
            )
        }

        // Carga inicial de eventos
        viewModel.fetchEvents(page = 1)
    }
}
