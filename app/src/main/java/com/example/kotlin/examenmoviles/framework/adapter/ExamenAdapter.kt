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

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = view.findViewById(R.id.eventDate)
        private val descriptionText: TextView = view.findViewById(R.id.eventDescription)
        private val categoryText: TextView = view.findViewById(R.id.eventCategory)
        private val moreDetails: TextView = view.findViewById(R.id.eventDetails)
        private var isExpanded = false

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

    fun updateData(nuevosEventos: List<EventoHistorico>) {
        eventos = nuevosEventos
        notifyDataSetChanged()
    }
}
