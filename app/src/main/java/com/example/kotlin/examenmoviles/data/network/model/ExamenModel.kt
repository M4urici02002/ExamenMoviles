package com.example.kotlin.examenmoviles.data.network.model

/**
 * Representa el modelo principal de un examen.
 *
 * @property result Contiene los datos históricos de los eventos.
 * @property code Código de estado de la respuesta.
 * @property error Mensaje de error (nulo si no hay error).
 */
data class ExamenModel(
    val result: ResultData? = null,
    val code: Int,
    val error: String? = null
)

/**
 * Representa los datos del resultado de un examen.
 *
 * @property count Número total de eventos en la página actual.
 * @property page Número de la página actual.
 * @property data Lista de eventos históricos obtenidos.
 */
data class ResultData(
    val count: Int,
    val page: Int,
    val data: List<EventoHistorico> = emptyList()
)

/**
 * Representa un evento histórico.
 *
 * @property date Fecha del evento.
 * @property description Descripción del evento.
 * @property lang Idioma del evento.
 * @property category1 Categoría principal del evento.
 * @property category2 Categoría secundaria del evento.
 * @property granularity Nivel de granularidad del evento (año, mes, día).
 * @property objectId Identificador único del evento.
 */
data class EventoHistorico(
    val date: String,
    val description: String,
    val lang: String,
    val category1: String,
    val category2: String,
    val granularity: String,
    val objectId: String
)
