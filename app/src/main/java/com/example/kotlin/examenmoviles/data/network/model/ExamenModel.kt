package com.example.kotlin.examenmoviles.data.network.model

data class ExamenModel(
    val result: ResultData? = null, // Resultado de datos históricos (nulo si hay error)
    val code: Int,                 // Código de estado
    val error: String? = null      // Mensaje de error (nulo si no hay error)
)

data class ResultData(
    val count: Int,                // Número de eventos en esta página
    val page: Int,                 // Página actual
    val data: List<EventoHistorico> = emptyList() // Lista de eventos históricos
)

data class EventoHistorico(
    val date: String,              // Fecha del evento
    val description: String,       // Descripción del evento
    val lang: String,              // Idioma
    val category1: String,         // Categoría principal
    val category2: String,         // Categoría secundaria
    val granularity: String,       // Granularidad (año, mes, día)
    val objectId: String           // ID único del objeto
)

