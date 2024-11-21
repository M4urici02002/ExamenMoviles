package com.example.kotlin.wushuapp.framework.viewmodel
import RequisitoRegistrarEstilo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel para manejar la lógica de registro de estilos en la aplicación.
 *
 * @property requisitoRegistrarEstilo Dependencia para manejar los requisitos del registro del estilo.
 */
open class RegistrarEstiloViewModel(
    private val requisitoRegistrarEstilo: RequisitoRegistrarEstilo = RequisitoRegistrarEstilo(),
) : ViewModel() {

    // LiveData para observar el resultado del registro
    private val _resultadoRegistro = MutableLiveData<Result<Unit>>()
    val resultadoRegistro: LiveData<Result<Unit>> get() = _resultadoRegistro

    // LiveData para observar errores de validación específicos
    private val _errorValidacion = MutableLiveData<String>()
    val errorValidacion: LiveData<String> get() = _errorValidacion

    /**
     * Registra un estilo de arte marcial en la base de datos, validando todos los campos antes de realizar la operación.
     *
     * @param estilo Datos del estilo a registrar.
     */
    fun registrarEstilo(estilo: Estilo) {
        // Validar campos obligatorios
        if (!validarNombre(estilo.nombre)) return
        if (!validarNombreChino(estilo.nombreChino)) return
        if (!validarDescripcion(estilo.descripcion)) return

        // Registrar el estilo si las validaciones son exitosas
        requisitoRegistrarEstilo.registrarEstilo(estilo) { resultado ->
            if (resultado.isSuccess) {
                _resultadoRegistro.postValue(Result.success(Unit))
            } else {
                // Analizar el mensaje de error devuelto por la Cloud Function
                val errorMessage = resultado.exceptionOrNull()?.message
                if (errorMessage != null && errorMessage.contains("El estilo con este nombre ya fue registrado")) {
                    _errorValidacion.postValue("El nombre del estilo ya está en uso. Elige otro.")
                } else if (errorMessage != null && errorMessage.contains("El nombre en chino del estilo ya está registrado")) {
                    _errorValidacion.postValue("El nombre en chino del estilo ya está en uso. Elige otro.")
                } else {
                    _resultadoRegistro.postValue(Result.failure(Throwable("Error al registrar el estilo: $errorMessage")))
                }
            }
        }
    }

    /**
     * Valida si el nombre es correcto.
     *
     * @param nombre Nombre a validar.
     * @return true si el nombre es válido, false en caso contrario.
     */
    private fun validarNombre(nombre: String): Boolean {
        if (nombre.isBlank()) {
            _errorValidacion.value = "El nombre del estilo es obligatorio."
            return false
        }
        return true
    }

    /**
     * Valida si el nombre en chino es correcto.
     *
     * @param nombreChino Nombre en chino a validar.
     * @return true si el nombre en chino es válido, false en caso contrario.
     */
    private fun validarNombreChino(nombreChino: String): Boolean {
        if (nombreChino.isBlank()) {
            _errorValidacion.value = "El nombre en chino del estilo es obligatorio."
            return false
        }
        return true
    }

    /**
     * Valida si la descripción es correcta.
     *
     * @param descripcion Descripción a validar.
     * @return true si la descripción es válida, false en caso contrario.
     */
    private fun validarDescripcion(descripcion: String): Boolean {
        if (descripcion.isBlank()) {
            _errorValidacion.value = "La descripción del estilo es obligatoria."
            return false
        }
        return true
    }
}
