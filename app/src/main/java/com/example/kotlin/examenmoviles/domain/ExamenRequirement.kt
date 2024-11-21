package com.example.kotlin.wushuapp.domain

/**
 * Clase encargada de manejar la lógica de negocio para obtener los estilos de Wushu.
 * Utiliza el repositorio para obtener la información necesaria.
 */

class ExamenRequirement(private val repository: EstiloRepository) {
    /**
     * Recupera la lista de estilos de Wushu disponibles a través del repositorio.
     *
     * Este método actúa como el punto de entrada desde la capa de presentación para
     * obtener los estilos, delegando la obtención de los datos al [EstiloRepository].
     *
     * @return Una lista de objetos [Estilo] representando los estilos disponibles. En caso de que no se encuentren estilos o haya algún error, se devolverá una lista vacía.
     */
    fun obtenerEstilos(): List<Estilo> {
        return repository.obtenerEstilos()
    }
}

