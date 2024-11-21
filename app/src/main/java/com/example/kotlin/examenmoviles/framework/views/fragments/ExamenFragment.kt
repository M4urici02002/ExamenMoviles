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

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        eventAdapter = EventAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = eventAdapter

        viewModel = ViewModelProvider(requireActivity()).get(ExamenViewModel::class.java)

        viewModel.eventos.observe(viewLifecycleOwner) { eventos ->
            eventAdapter.updateData(eventos)
        }

        viewModel.error.observe(viewLifecycleOwner) { mensajeError ->
            Toast.makeText(requireContext(), mensajeError, Toast.LENGTH_LONG).show()
        }

        viewModel.filteredEvents.observe(viewLifecycleOwner) { filtered ->
            eventAdapter.updateData(filtered)
        }

        viewModel.fetchEvents(page = 1)
    }
}
