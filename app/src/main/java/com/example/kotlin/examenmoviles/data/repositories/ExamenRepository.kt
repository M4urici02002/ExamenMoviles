package com.example.kotlin.examenmoviles.data.repositories

import com.example.kotlin.examenmoviles.data.network.NetworkModuleDI
import com.example.kotlin.examenmoviles.data.network.model.ExamenModel

class ExamenRepository {

    /**
     * Función para obtener eventos históricos desde la función en la nube "hello".
     *
     * @param page Número de página a consultar.
     * @param onResult Callback para manejar el resultado o el error.
     */
    fun fetchHistoricalEvents(
        page: Int,
        onResult: (ExamenModel?, Exception?) -> Unit
    ) {
        // Parámetros de la solicitud
        val parametros = hashMapOf("page" to page)

        // Llama a la función en la nube usando NetworkModuleDI
        NetworkModuleDI.callCloudFunctionWithRetry<ExamenModel>(
            nombreFuncion = "hello",
            parametros = parametros,
            maxReintentos = 3
        ) { resultado, error ->
            if (resultado != null) {
                // Devuelve el resultado exitoso
                onResult(resultado, null)
            } else {
                // Devuelve el error en caso de fallo
                onResult(null, error)
            }
        }
    }
}
