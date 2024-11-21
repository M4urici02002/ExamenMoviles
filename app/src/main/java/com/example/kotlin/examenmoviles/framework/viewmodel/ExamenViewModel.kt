package com.example.kotlin.examenmoviles.framework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.examenmoviles.data.network.model.EventoHistorico
import com.example.kotlin.examenmoviles.data.repositories.ExamenRepository
import com.example.kotlin.examenmoviles.domain.FilterHistoricalEventsUseCase
import kotlinx.coroutines.launch

class ExamenViewModel : ViewModel() {

    private val repository = ExamenRepository()
    private val filterUseCase = FilterHistoricalEventsUseCase()

    private val _eventos = MutableLiveData<List<EventoHistorico>>()
    val eventos: LiveData<List<EventoHistorico>> get() = _eventos

    private val _filteredEvents = MutableLiveData<List<EventoHistorico>>()
    val filteredEvents: LiveData<List<EventoHistorico>> get() = _filteredEvents

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

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

    fun filterEvents(place: String?, dateRange: String?) {
        val filtered = filterUseCase.execute(
            _eventos.value ?: emptyList(),
            place,
            null, null
        ).filter { event ->
            dateRange == null || dateRange == "Todos" || isInRange(event.date, dateRange)
        }
        _filteredEvents.postValue(filtered)
    }

    private fun isInRange(date: String, range: String): Boolean {
        val year = date.toIntOrNull() ?: return false
        val (start, end) = range.split(" a ").map { it.toInt() }
        return year in start..end
    }
}
