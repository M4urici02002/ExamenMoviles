package com.example.kotlin.data.network

import android.content.Context
import com.example.kotlin.examenmoviles.utils.Constants.APPLICATION_ID
import com.example.kotlin.examenmoviles.utils.Constants.BASE_URL
import com.example.kotlin.examenmoviles.utils.Constants.CLIENT_KEY
import com.parse.Parse
import com.parse.ParseCloud
import com.parse.ParseException

/**
 * Módulo de red para la integración con Parse, incluyendo inicialización y llamadas a funciones en la nube.
 */
object NetworkModuleDI {

    private var isParseInitialized = false

    /**
     * Inicializa Parse con la configuración necesaria para la conexión al backend.
     *
     * @param context El contexto de la aplicación utilizado para la configuración de Parse.
     */
    fun initializeParse(context: Context) {
        try {
            Parse.initialize(
                Parse.Configuration
                    .Builder(context)
                    .applicationId(APPLICATION_ID)
                    .clientKey(CLIENT_KEY)
                    .server(BASE_URL)
                    .build()
            )
            isParseInitialized = true
            println("Parse inicializado correctamente.")
        } catch (e: Exception) {
            println("Error al inicializar Parse: ${e.message}")
        }
    }

    /**
     * Llama a una función en la nube de Parse con manejo de reintentos en caso de error.
     *
     * @param nombreFuncion Nombre de la función en la nube a invocar.
     * @param parametros Parámetros necesarios para la función.
     * @param maxReintentos Número máximo de reintentos en caso de error.
     * @param callback Callback que recibe el resultado o un error.
     * @param T Tipo de dato esperado como resultado de la función en la nube.
     */
    fun <T> callCloudFunctionWithRetry(
        nombreFuncion: String,
        parametros: HashMap<String, Int>,
        maxReintentos: Int = 3,
        callback: (T?, Exception?) -> Unit
    ) {
        if (!isParseInitialized) {
            callback(null, IllegalStateException("Parse no ha sido inicializado. Llama a initializeParse primero."))
            return
        }

        if (parametros.isEmpty()) {
            callback(null, IllegalArgumentException("Los parámetros no pueden estar vacíos."))
            return
        }

        var intentos = 0

        /**
         * Realiza la llamada a la función en la nube.
         */
        fun realizarLlamada() {
            println("Llamando a la función '$nombreFuncion' con parámetros: $parametros. Intento: ${intentos + 1}")

            try {
                ParseCloud.callFunctionInBackground<T>(nombreFuncion, parametros) { resultado, error ->
                    if (error == null) {
                        println("Llamada exitosa. Resultado: $resultado")
                        callback(resultado, null)
                    } else {
                        intentos++
                        println("Error en la llamada: ${error.message}. Intento $intentos de $maxReintentos.")

                        // Validación de errores comunes
                        when (error.code) {
                            ParseException.CONNECTION_FAILED -> println("Error: Falló la conexión al servidor.")
                            ParseException.OBJECT_NOT_FOUND -> println("Error: Objeto no encontrado.")
                            ParseException.TIMEOUT -> println("Error: Tiempo de espera agotado.")
                            else -> println("Error desconocido: ${error.message}")
                        }

                        if (intentos < maxReintentos) {
                            realizarLlamada()
                        } else {
                            println("Error definitivo tras $maxReintentos intentos: ${error.message}")
                            callback(null, error)
                        }
                    }
                }
            } catch (e: Exception) {
                println("Excepción no controlada: ${e.message}")
                callback(null, e)
            }
        }

        realizarLlamada()
    }
}
