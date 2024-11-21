package com.example.kotlin.examenmoviles.data.network

import com.example.kotlin.examenmoviles.data.network.model.ExamenModel200
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /**
     * Endpoint para obtener los eventos históricos paginados
     * @param page Número de la página solicitada
     * @return Respuesta con los datos del modelo ExamenModel200
     */
    @GET("functions/hello")
    suspend fun getHistoricalEvents(
        @Query("page") page: Int
    ): Response<ExamenModel200>
}

