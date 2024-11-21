package com.example.kotlin.wushuapp.data.repositories

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.kotlin.wushuapp.data.network.NetworkModuleDI
import com.parse.ParseException

/**
 * Repositorio encargado de manejar las operaciones relacionadas con los alumnos.
 * Ej. Registro de un nuevo alumno en el sistema a través de la cloud function registrarAlumno.
 */
class ExamenRepository {
    /**
     * Registra un alumno en el sistema enviando los datos a la cloud function registrarAlumno.
     *
     * @param nombre El nombre completo del alumno.
     * @param correo El correo electrónico del alumno.
     * @param fechaNacimiento La fecha de nacimiento del alumno en formato "DD-MM-YYYY".
     * @param telefono El número de teléfono del alumno (10 dígitos).
     * @param curp La CURP del alumno (18 caracteres).
     * @param direccion La dirección residencial del alumno.
     * @param peso El peso del alumno en kilogramos (no puede ser nulo).
     * @param fuerza La fuerza física del alumno (valor opcional).
     * @param elasticidad La elasticidad física del alumno (valor opcional).
     * @param resistencia La resistencia física del alumno (valor opcional).
     * @param capacidadCardiopulmonar La capacidad cardiopulmonar del alumno (valor opcional).
     * @param estatura La estatura del alumno en centimetros (valor opcional).
     * @param colegiatura El costo de la colegiatura del alumno (valor opcional).
     * @param callback Función que recibe un [Result] indicando el éxito o el error de la operación.
     */
    fun registrarAlumno(
        RegistroAlumno: RegistroAlumno,
        context: Context,
        callback: (Result<Unit>) -> Unit,
    ) {
        // Mapear los parámetros obligatorios
        val parametros =
            hashMapOf<String, Any>(
                "nombre" to RegistroAlumno.nombre,
                "correoElectronico" to RegistroAlumno.correo,
                "fechaNacimiento" to RegistroAlumno.fechaNacimiento,
                "telefono" to RegistroAlumno.telefono,
                "curp" to RegistroAlumno.curp,
                "direccion" to RegistroAlumno.direccion,
                "peso" to RegistroAlumno.peso!!,
            )

        // Agregar parámetros opcionales si están disponibles
        if (RegistroAlumno.fuerza != null) parametros["fuerza"] = RegistroAlumno.fuerza
        if (RegistroAlumno.elasticidad != null) {
            parametros["elasticidad"] =
                RegistroAlumno.elasticidad
        }
        if (RegistroAlumno.resistencia != null) {
            parametros["resistencia"] =
                RegistroAlumno.resistencia
        }
        if (RegistroAlumno.capacidadCardiopulmonar != null) {
            parametros["capacidadCardiopulmonar"] =
                RegistroAlumno.capacidadCardiopulmonar
        }
        if (RegistroAlumno.estatura != null) parametros["estatura"] = RegistroAlumno.estatura
        if (RegistroAlumno.colegiatura != null) {
            parametros["colegiatura"] =
                RegistroAlumno.colegiatura
        }

        // Llamar a la función en la nube para registrar al alumno
        NetworkModuleDI.callCloudFunction<Unit>("registrarAlumno", parametros) { result, e ->
            if (e == null) {
                callback(Result.success(Unit))
            } else {
                // Manejar excepciones de ParseException
                if (e is ParseException && e.code == 1) {
                    callback(Result.failure(Exception("Error de conexión con la base de datos")))
                } else if (e is ParseException && e.code == 100) {
                    val intent = Intent(context, NoConexionActivity::class.java)
                    context.startActivity(intent)
                    callback(Result.failure(Exception("Error de conexión")))
                } else {
                    callback(Result.failure(e))
                }
            }
        }
    }

    /**
     * Obtiene los datos de un alumno utilizando su ID y los mapea a una instancia de `RegistroAlumno`.
     *
     * Esta función realiza una llamada a una función en la nube (cloud function) para obtener los datos
     * de un alumno específico. Si la llamada es exitosa, mapea los datos a un objeto `RegistroAlumno`
     * y lo pasa al callback como un `Result.success`. Si ocurre un error, pasa la excepción al callback
     * como un `Result.failure`.
     *
     * @param alumnoId ID del alumno a obtener.
     * @param callback Función que recibe el resultado de la operación. Retorna un `Result` que contiene
     *                 un objeto `RegistroAlumno` si tiene éxito, o un error si ocurre una falla.
     */
    fun obtenerAlumno(
        alumnoId: String,
        callback: (Result<RegistroAlumno>) -> Unit,
    ) {
        val parametros =
            hashMapOf<String, Any>(
                "idAlumno" to alumnoId,
            )

        // Llamada a la función en la nube que obtiene los datos del alumno.
        NetworkModuleDI.callCloudFunction<Map<String, Any>>(
            "alumnoAModificar",
            parametros,
        ) { resultado, e ->
            if (e == null && resultado != null) {
                try {
                    // Mapea los datos del resultado a una instancia de RegistroAlumno
                    val alumno =
                        RegistroAlumno(
                            nombre = resultado["nombre"] as String,
                            correo = resultado["correo"] as String,
                            fechaNacimiento = "",
                            telefono = resultado["telefono"] as String,
                            curp = resultado["curp"] as String,
                            direccion = resultado["direccion"] as String,
                            peso = (resultado["peso"] as? Number)?.toDouble(),
                            fuerza = (resultado["fuerza"] as? Number)?.toDouble(),
                            elasticidad = (resultado["elasticidad"] as? Number)?.toDouble(),
                            resistencia = (resultado["resistencia"] as? Number)?.toDouble(),
                            capacidadCardiopulmonar = (resultado["capacidadCardiopulmonar"] as? Number)?.toDouble(),
                            estatura = (resultado["estatura"] as? Number)?.toDouble(),
                            colegiatura = (resultado["colegiatura"] as? Number)?.toDouble(),
                        )

                    callback(Result.success(alumno))
                } catch (ex: Exception) {
                    // Si hay algún error en el mapeo, se pasa al callback.
                    callback(Result.failure(ex))
                }
            } else {
                // Manejo de errores según el tipo de excepción.
                if (e is ParseException && e.code == 1) {
                    callback(Result.failure(Exception("Error de conexión con la base de datos")))
                } else if (e is ParseException && e.code == 100) {
                    callback(Result.failure(Exception("Error de conexión")))
                } else {
                    callback(Result.failure(e ?: Exception("Error desconocido")))
                }
            }
        }
    }

    /**
     * Actualiza los datos de un alumno en el sistema.
     *
     * Esta función realiza una llamada a una función en la nube (cloud function) para actualizar la
     * información de un alumno en el backend. Si la operación es exitosa, pasa un `Result.success` al
     * callback. Si ocurre un error, pasa el error al callback como un `Result.failure`.
     *
     * @param alumnoId ID del alumno que se actualizará.
     * @param alumno Objeto `RegistroAlumno` que contiene los datos actualizados del alumno.
     * @param callback Función que recibe el resultado de la operación. Retorna un `Result` de tipo `Unit`
     *                 si la actualización es exitosa, o un error si ocurre una falla.
     */
    fun actualizarAlumno(
        alumnoId: String,
        alumno: RegistroAlumno,
        curpBandera: Boolean,
        callback: (Result<Unit>) -> Unit,
    ) {
        Log.d("Bandera de curp", curpBandera.toString())
        val parametros =
            hashMapOf<String, Any>(
                "idUsuarioActual" to alumnoId,
                "nombre" to alumno.nombre,
                "correoElectronico" to alumno.correo,
                "fechaNacimiento" to alumno.fechaNacimiento,
                "telefono" to alumno.telefono,
                "curp" to alumno.curp,
                "direccion" to alumno.direccion,
                "peso" to (alumno.peso ?: 0.0) as Any,
                "fuerza" to (alumno.fuerza ?: 0.0) as Any,
                "elasticidad" to (alumno.elasticidad ?: 0.0) as Any,
                "resistencia" to (alumno.resistencia ?: 0.0) as Any,
                "capacidadCardiopulmonar" to (alumno.capacidadCardiopulmonar ?: 0.0) as Any,
                "colegiatura" to (alumno.colegiatura ?: 0.0) as Any,
                "estatura" to (alumno.estatura ?: 0.0) as Any,
                "curpBandera" to curpBandera,
            )

        // Llamada a la función en la nube que actualiza los datos del alumno.
        NetworkModuleDI.callCloudFunction<String>(
            "ActualizarAlumno",
            parametros,
        ) { resultado, error ->
            if (error == null) {
                callback(Result.success(Unit)) // Si es exitoso, pasa un resultado de éxito.
            } else {
                callback(Result.failure(error)) // Si ocurre un error, pasa el error al callback.
            }
        }
    }
}
