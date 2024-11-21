package com.example.kotlin.examenmoviles.data.network

import android.content.Context
import com.example.kotlin.examenmoviles.utils.Constants.APPLICATION_ID
import com.example.kotlin.examenmoviles.utils.Constants.BASE_URL
import com.example.kotlin.examenmoviles.utils.Constants.CLIENT_KEY
import com.parse.Parse
import com.parse.ParseCloud

object NetworkModuleDI {
    /**
     * Inicializa Parse con la configuración necesaria para la conexión al backend.
     *
     * @param context El contexto de la aplicación utilizado para la configuración de Parse.
     */
    fun initializeParse(context: Context) {
        Parse.initialize(
            Parse.Configuration
                .Builder(context)
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server(BASE_URL)
                .build()
        )
    }

    /**
     * Llama a una función en la nube de Parse con manejo de reintentos en caso de error.
     *
     * @param nombreFuncion Nombre de la función en la nube a invocar.
     * @param parametros Parámetros necesarios para la función.
     * @param maxReintentos Número máximo de reintentos en caso de error.
     * @param callback Callback con el resultado o el error.
     */
    fun <T> callCloudFunctionWithRetry(
        nombreFuncion: String,
        parametros: HashMap<String, Int>,
        maxReintentos: Int = 3,
        callback: (T?, Exception?) -> Unit
    ) {
        var intentos = 0

        fun realizarLlamada() {
            ParseCloud.callFunctionInBackground<T>(nombreFuncion, parametros) { resultado, error ->
                if (error == null) {
                    // Éxito: devolver el resultado
                    callback(resultado, null)
                } else {
                    intentos++
                    if (intentos < maxReintentos) {
                        // Log para depuración
                        println("Intento $intentos fallido. Reintentando...")
                        realizarLlamada()
                    } else {
                        // Fallo después de reintentar
                        callback(null, error)
                    }
                }
            }
        }

        realizarLlamada()
    }
}
