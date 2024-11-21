package com.example.kotlin.examenmoviles.domain

import com.example.kotlin.examenmoviles.data.network.model.EventoHistorico

/**
 * Caso de uso para filtrar eventos históricos basados en diferentes criterios.
 */
class FilterHistoricalEventsUseCase {

    /**
     * Filtra una lista de eventos históricos en base a los parámetros proporcionados.
     *
     * @param events Lista de eventos a filtrar.
     * @param category Categoría por la cual filtrar (nulo si no se aplica filtro).
     * @param startDate Fecha de inicio para el filtro (nulo si no se aplica filtro).
     * @param endDate Fecha de fin para el filtro (nulo si no se aplica filtro).
     * @return Lista filtrada de eventos históricos.
     */
    fun execute(
        events: List<EventoHistorico>,
        category: String?,
        startDate: String?,
        endDate: String?
    ): List<EventoHistorico> {
        return events.filter { event ->
            (category == null || event.category1 == category || event.category2 == category) &&
                    (startDate == null || event.date >= startDate) &&
                    (endDate == null || event.date <= endDate)
        }
    }
}
