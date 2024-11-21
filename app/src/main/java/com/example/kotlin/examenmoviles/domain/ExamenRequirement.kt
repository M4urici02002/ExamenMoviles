package com.example.kotlin.examenmoviles.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlin.examenmoviles.data.network.model.EventoHistorico
import com.example.kotlin.examenmoviles.data.repositories.ExamenRepository

class ExamenRequirement(application: Application) : AndroidViewModel(application) {

    private val repository = ExamenRepository()

    // LiveData para exponer eventos históricos a la UI
    private val _eventos = MutableLiveData<List<EventoHistorico>>()
    val eventos: LiveData<List<EventoHistorico>> get() = _eventos

    // LiveData para manejar errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    /**
     * Obtiene eventos históricos desde el repositorio y actualiza los LiveData.
     *
     * @param page Número de página a consultar.
     */
    fun fetchEvents(page: Int) {
        repository.fetchHistoricalEvents(page) { resultado, excepcion ->
            if (resultado != null) {
                _eventos.postValue(resultado.result?.data ?: emptyList())
            } else {
                _error.postValue(excepcion?.message ?: "Error desconocido")
            }
        }
    }
}
