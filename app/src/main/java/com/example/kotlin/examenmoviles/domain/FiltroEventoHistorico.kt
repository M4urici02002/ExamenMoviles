package com.example.kotlin.examenmoviles.domain

import com.example.kotlin.examenmoviles.data.network.model.EventoHistorico

class FilterHistoricalEventsUseCase {
    /**
     * Filters the list of historical events based on the provided parameters.
     *
     * @param events The list of events to filter.
     * @param category The category to filter by (null for no filter).
     * @param startDate The start date for filtering (null for no filter).
     * @param endDate The end date for filtering (null for no filter).
     * @return The filtered list of events.
     */
    fun execute(events: List<EventoHistorico>, category: String?, startDate: String?, endDate: String?): List<EventoHistorico> {
        return events.filter { event ->
            (category == null || event.category1 == category || event.category2 == category) &&
                    (startDate == null || event.date >= startDate) &&
                    (endDate == null || event.date <= endDate)
        }
    }
}

