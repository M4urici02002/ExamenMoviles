package com.example.kotlin.examenmoviles.framework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examenmoviles.R
import com.example.kotlin.examenmoviles.data.network.model.EventoHistorico

/**
 * Adaptador para mostrar una lista de eventos históricos en un RecyclerView.
 *
 * @property eventos Lista de eventos históricos a mostrar.
 */
class EventAdapter(
    private var eventos: List<EventoHistorico> = emptyList()
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    /**
     * ViewHolder para representar un evento histórico.
     *
     * @param view Vista que contiene los elementos del evento.
     */
    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = view.findViewById(R.id.eventDate)
        private val descriptionText: TextView = view.findViewById(R.id.eventDescription)
        private val categoryText: TextView = view.findViewById(R.id.eventCategory)
        private val moreDetails: TextView = view.findViewById(R.id.eventDetails)
        private var isExpanded = false

        /**
         * Vincula un evento histórico a los elementos de la vista.
         *
         * @param evento Evento histórico a mostrar.
         */
        fun bind(evento: EventoHistorico) {
            dateText.text = evento.date
            descriptionText.text = evento.description
            categoryText.text = "${evento.category1} - ${evento.category2}"

            itemView.setOnClickListener {
                isExpanded = !isExpanded
                moreDetails.visibility = if (isExpanded) View.VISIBLE else View.GONE
                moreDetails.text = "Language: ${evento.lang}\nGranularity: ${evento.granularity}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventos[position])
    }

    override fun getItemCount(): Int = eventos.size

    /**
     * Actualiza la lista de eventos y notifica al adaptador para redibujar la vista.
     *
     * @param nuevosEventos Nueva lista de eventos históricos.
     */
    fun updateData(nuevosEventos: List<EventoHistorico>) {
        eventos = nuevosEventos
        notifyDataSetChanged()
    }
}
