package com.example.kotlin.examenmoviles.framework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.examenmoviles.data.network.model.EventoHistorico
import com.example.kotlin.examenmoviles.data.repositories.ExamenRepository
import kotlinx.coroutines.launch

class ExamenViewModel : ViewModel() {

    private val repository = ExamenRepository()

    // LiveData para los eventos históricos
    private val _eventos = MutableLiveData<List<EventoHistorico>>()
    val eventos: LiveData<List<EventoHistorico>> get() = _eventos

    // LiveData para manejar errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Función para obtener eventos históricos
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
}