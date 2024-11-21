package com.example.kotlin.examenmoviles.framework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examenmoviles.R
import com.example.kotlin.examenmoviles.data.network.model.EventoHistorico

class EventAdapter(
    private var eventos: List<EventoHistorico> = emptyList()
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // ViewHolder para representar cada evento
    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = view.findViewById(R.id.eventDate)
        private val descriptionText: TextView = view.findViewById(R.id.eventDescription)
        private val categoryText: TextView = view.findViewById(R.id.eventCategory)

        // Vincula los datos del evento a las vistas
        fun bind(evento: EventoHistorico) {
            dateText.text = evento.date
            descriptionText.text = evento.description
            categoryText.text = "${evento.category1} - ${evento.category2}"
        }
    }

    // Crea una nueva vista para un elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    // Vincula los datos a la vista existente
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventos[position])
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int = eventos.size

    // Actualiza los datos del adaptador
    fun updateData(nuevosEventos: List<EventoHistorico>) {
        eventos = nuevosEventos
        notifyDataSetChanged()
    }
}