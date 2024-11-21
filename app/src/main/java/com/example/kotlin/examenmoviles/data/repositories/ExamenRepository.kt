package com.example.kotlin.examenmoviles.data.repositories

import com.example.kotlin.data.network.NetworkModuleDI
import com.example.kotlin.examenmoviles.data.network.model.ExamenModel
import com.example.kotlin.examenmoviles.data.network.model.EventoHistorico
import com.example.kotlin.examenmoviles.data.network.model.ResultData
import com.parse.ParseObject

/**
 * Repositorio para manejar la obtención de eventos históricos desde el backend.
 */
class ExamenRepository {

    /**
     * Obtiene una lista de eventos históricos desde el backend utilizando Parse.
     *
     * @param page Número de la página que se desea obtener.
     * @param onResult Callback que devuelve un modelo [ExamenModel] en caso de éxito o una excepción en caso de error.
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
                    // Extrae la lista de objetos ParseObject
                    val dataList = resultado["data"] as? List<ParseObject> ?: emptyList()

                    // Mapea los ParseObjects a tu modelo EventoHistorico
                    val eventos = dataList.map { parseObject ->
                        EventoHistorico(
                            date = parseObject.getString("date") ?: "",
                            description = parseObject.getString("description") ?: "",
                            lang = parseObject.getString("lang") ?: "",
                            category1 = parseObject.getString("category1") ?: "",
                            category2 = parseObject.getString("category2") ?: "",
                            granularity = parseObject.getString("granularity") ?: "",
                            objectId = parseObject.objectId
                        )
                    }

                    // Crea el modelo ResultData
                    val resultData = ResultData(
                        count = eventos.size,
                        page = page,
                        data = eventos
                    )

                    // Crea el modelo ExamenModel
                    val examenModel = ExamenModel(
                        result = resultData,
                        code = resultado["code"] as? Int ?: 0
                    )

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
