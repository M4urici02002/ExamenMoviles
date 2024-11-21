package com.example.kotlin.examenmoviles.data.repositories

import com.example.kotlin.examenmoviles.data.network.NetworkModuleDI
import com.example.kotlin.examenmoviles.data.network.model.ExamenModel
import com.google.gson.Gson

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
        val parametros = hashMapOf("page" to page)

        NetworkModuleDI.callCloudFunctionWithRetry<HashMap<String, Any>>(
            nombreFuncion = "hello",
            parametros = parametros,
            maxReintentos = 3
        ) { resultado, error ->
            if (resultado != null) {
                try {
                    // Convierte el HashMap a JSON y luego a ExamenModel
                    val gson = Gson()
                    val json = gson.toJson(resultado)
                    val examenModel = gson.fromJson(json, ExamenModel::class.java)
                    onResult(examenModel, null)
                } catch (e: Exception) {
                    onResult(null, e)
                }
            } else {
                onResult(null, error)
            }
        }
    }
}
