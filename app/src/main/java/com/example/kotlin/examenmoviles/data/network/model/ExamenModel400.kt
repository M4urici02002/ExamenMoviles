package com.example.kotlin.examenmoviles.data.network.model

class ExamenModel400 (
    val result: ResultData
)

data class ResultData(
    val code: Int,            // Código de estado
    val count: Int,           // Número de eventos en esta página
    val page: Int,            // Página actual
    val data: List<ExamenModel400> // Lista de eventos históricos
)

data class EventoHistorico(
    val date: String,         // Fecha del evento
    val description: String,  // Descripción del evento
    val lang: String,         // Idioma
    val category1: String,    // Categoría principal
    val category2: String,    // Categoría secundaria
    val granularity: String,  // Granularidad (año, mes, día)
    val objectId: String      // ID único del objeto
)
