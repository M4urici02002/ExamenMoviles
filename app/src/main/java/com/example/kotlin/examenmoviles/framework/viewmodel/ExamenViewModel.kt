package com.example.kotlin.examenmoviles.framework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.examenmoviles.data.network.model.EventoHistorico
import com.example.kotlin.examenmoviles.data.repositories.ExamenRepository
import com.example.kotlin.examenmoviles.domain.FilterHistoricalEventsUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de la aplicación para la obtención y filtrado de eventos históricos.
 */
class ExamenViewModel : ViewModel() {

    private val repository = ExamenRepository()
    private val filterUseCase = FilterHistoricalEventsUseCase()

    // LiveData para exponer la lista de eventos históricos originales
    private val _eventos = MutableLiveData<List<EventoHistorico>>()
    val eventos: LiveData<List<EventoHistorico>> get() = _eventos

    // LiveData para exponer la lista de eventos históricos filtrados
    private val _filteredEvents = MutableLiveData<List<EventoHistorico>>()
    val filteredEvents: LiveData<List<EventoHistorico>> get() = _filteredEvents

    // LiveData para manejar errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    /**
     * Obtiene eventos históricos desde el repositorio y actualiza los LiveData correspondientes.
     *
     * @param page Número de página a consultar.
     */
    fun fetchEvents(page: Int) {
        viewModelScope.launch {
            repository.fetchHistoricalEvents(page) { resultado, excepcion ->
                if (resultado != null) {
                    _eventos.postValue(resultado.result?.data ?: emptyList())
                } else {
                    _error.postValue(excepcion?.message ?: "Error desconocido")
                }
            }
        }
    }

    /**
     * Filtra la lista de eventos históricos en base a la ubicación y un rango de fechas.
     *
     * @param place Ubicación por la cual filtrar (nulo si no se aplica filtro).
     * @param dateRange Rango de fechas en formato "inicio a fin" (nulo si no se aplica filtro).
     */
    fun filterEvents(place: String?, dateRange: String?) {
        val filtered = filterUseCase.execute(
            _eventos.value ?: emptyList(),
            place,
            null,
            null
        ).filter { event ->
            dateRange == null || dateRange == "Todos" || isInRange(event.date, dateRange)
        }
        _filteredEvents.postValue(filtered)
    }

    /**
     * Verifica si una fecha está dentro de un rango especificado.
     *
     * @param date Fecha en formato de cadena.
     * @param range Rango de fechas en formato "inicio a fin".
     * @return Verdadero si la fecha está dentro del rango, falso en caso contrario.
     */
    private fun isInRange(date: String, range: String): Boolean {
        val year = date.toIntOrNull() ?: return false
        val (start, end) = range.split(" a ").map { it.toInt() }
        return year in start..end
    }
}
