package com.example.kotlin.wushuapp.framework.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin.wushuapp.R
import com.example.kotlin.wushuapp.databinding.FragmentBarraLateralBinding

/**
 * Fragmento que representa la barra lateral de la interfaz de usuario. Gestiona la visualización
 * de los elementos de la barra lateral dependiendo de los privilegios del usuario.
 */
class ExamenFragment : Fragment() {
    private var _binding: FragmentBarraLateralBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BarraLateralViewModel

    /**
     * Método llamado cuando se crea la vista del fragmento.
     *
     * @param inflater El objeto [LayoutInflater] que se utiliza para inflar la vista del fragmento.
     * @param container El contenedor de la vista del fragmento.
     * @param savedInstanceState El estado previamente guardado del fragmento.
     * @return La vista del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[BarraLateralViewModel::class.java]
        // Se obtienen los privilegios del usuario.
        viewModel.obtenerPrivilegios(VariablesGlobales.correoUsuario.toString(), requireContext())

        _binding = FragmentBarraLateralBinding.inflate(inflater, container, false)
        val root: View = binding.root

        inicializarObservers()
        inicializarListeners()

        return root
    }

    /**
     * Método llamado cuando la vista del fragmento es destruida.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Inicializa los listeners de la interfaz de usuario.
     */
    private fun inicializarListeners() {
        binding.estilos.setOnClickListener {
            pasarVista(ConsultarEstilosActivity::class.java)
        }

        binding.elementosGuardados.setOnClickListener {
            // TODO: Implementar elementos guardados
        }

        binding.academia.setOnClickListener {
            pasarVista(AcademiaActivity::class.java)
        }

        binding.configuracion.setOnClickListener {
            // TODO: Implementar configuración
        }

        binding.cerrarSesion.setOnClickListener {
            // TODO: Implementar cierre de sesión
        }
    }

    /**
     * Pasa a la vista de la actividad especificada.
     *
     * @param claseActivity La clase de la actividad a la que se desea pasar.
     */
    private fun pasarVista(claseActivity: Class<*>) {
        val intent = Intent(activity, claseActivity)
        startActivity(intent)
    }

    /**
     * Inicializa los observers de la vista.
     */
    private fun inicializarObservers() {
        // Observa los cambios en los privilegios del usuario.
        viewModel.privilegios.observe(viewLifecycleOwner) { privilegios ->
            configurarVista(privilegios, obtenerListaItems())
        }

        // Observa los cambios en el mensaje de error.
        viewModel.errorMensaje.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Configura la vista de la barra lateral en función de los privilegios del usuario.
     * Si el usuario no tiene el privilegio para consultar la academia, se ocultan los
     * elementos relacionados con la academia y se reorganizan los elementos de la barra lateral.
     *
     * @param privilegios La lista de privilegios del usuario.
     */
    private fun configurarVista(
        privilegios: List<String>,
        listaItems: List<String>,
    ) {
        if (privilegios.contains(Privilegios.CONSULTAR_ESTILOS.privilegio)) {
            binding.estilos.visibility = View.VISIBLE
        } else {
            reorganizarItems("estilos", listaItems)
        }
        if (privilegios.contains(Privilegios.CONSULTAR_ELEMENTOS_GUARDADOS.privilegio)) {
            binding.elementosGuardados.visibility = View.VISIBLE
        } else {
            reorganizarItems("elementosGuardados", listaItems)
        }
        if (privilegios.contains(Privilegios.CONSULTAR_CONFIGURACION.privilegio)) {
            binding.configuracion.visibility = View.VISIBLE
        } else {
            reorganizarItems("configuracion", listaItems)
        }
        if (privilegios.contains(Privilegios.CONSULTAR_ACADEMIA.privilegio)) {
            binding.academia.visibility = View.VISIBLE
        } else {
            reorganizarItems("academia", listaItems)
        }
    }

    /**
     * Obtiene la lista de los elementos de la barra lateral. Esta lista se usa para reorganizar
     * los elementos de la interfaz según los privilegios del usuario.
     *
     * @return Una lista de los elementos de la barra lateral.
     */
    private fun obtenerListaItems(): List<String> {
        val context = requireContext()
        val layoutInflater = LayoutInflater.from(context)
        val vista = layoutInflater.inflate(R.layout.fragment_barra_lateral, null) as ConstraintLayout
        return obtenerOrdenItems(vista)
    }

    /**
     * Reorganiza los elementos de la barra lateral, ajustando sus parámetros de layout
     * según la visibilidad de los elementos.
     *
     * @param desdeElemento El nombre del elemento desde el cual empezar a reorganizar.
     * @param items La lista de elementos que forman la barra lateral.
     */
    private fun reorganizarItems(
        desdeElemento: String,
        items: List<String>,
    ) {
        val indiceInicio = items.indexOf(desdeElemento)

        for (i in indiceInicio + 1 until items.size) {
            // Obtener los identificadores de los elementos anteriores
            val anteriorGuidelineId = resources.getIdentifier("${items[i - 1]}_guideline", "id", context?.packageName)
            val dosAnteriorGuidelineId =
                if (i > 1) {
                    resources.getIdentifier("${items[i - 2]}_guideline", "id", context?.packageName)
                } else {
                    null
                }

            // Obtener el identificador del elemento actual
            val currentItemId = resources.getIdentifier(items[i], "id", context?.packageName)
            val currentItem = binding.root.findViewById<View>(currentItemId)

            // Actualizar los parámetros de layout del elemento actual
            currentItem.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomToTop = anteriorGuidelineId
                topToTop = dosAnteriorGuidelineId ?: ConstraintLayout.LayoutParams.UNSET
            }
        }
    }

    /**
     * Obtiene el orden de los elementos en la vista de la barra lateral. Se utiliza para
     * crear la lista de los items en la barra lateral.
     *
     * @param layout El [ConstraintLayout] que contiene los elementos.
     * @return Una lista de los nombres de los items presentes en la barra lateral.
     */
    private fun obtenerOrdenItems(layout: ConstraintLayout): List<String> {
        val ids = mutableListOf<String>()
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            val id = child.id
            if (id != View.NO_ID) {
                val nombreRecurso = resources.getResourceEntryName(id)
                if (!nombreRecurso.endsWith("_guideline") && nombreRecurso != "cerrar_sesion") {
                    ids.add(nombreRecurso)
                }
            }
        }
        return ids
    }
}
