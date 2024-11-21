package com.example.kotlin.examenmoviles.data.network.model

// Clase para manejar respuestas de error
data class ExamenModel200(
    val code: Int,         // Código de error
    val error: String      // Mensaje de error
)
