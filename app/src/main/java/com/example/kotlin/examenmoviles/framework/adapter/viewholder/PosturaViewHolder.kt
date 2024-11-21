package com.example.kotlin.wushuapp.framework.views.viewholders

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin.wushuapp.databinding.DialogoConfirmacionEliminarPosturaBinding
import com.example.kotlin.wushuapp.databinding.ItemPosturaBinding

/**
 * ViewHolder para mostrar los elementos de la lista de posturas en el RecyclerView.
 * Gestiona el enlace de los datos con las vistas de cada ítem del RecyclerView.
 *
 * @property binding Objeto de enlace de vista generado para el diseño de un ítem de postura (ItemPosturaBinding).
 * @property onButtonClick Callback que se invoca cuando el usuario hace clic en el botón circular.
 */
class PosturaViewHolder(
    private val binding: ItemPosturaBinding,
    private val eliminarPosturaViewModel: EliminarPosturaViewModel,
    private val onButtonClick: (String) -> Unit, // Callback para el botón
) : RecyclerView.ViewHolder(binding.root) {
    /**
     * Asocia un objeto `Postura` con las vistas correspondientes en el ítem.
     *
     * @param postura El objeto `Postura` que contiene los datos a mostrar.
     * @param context El contexto necesario para cargar la imagen con Glide.
     */
    fun bind(
        postura: Postura,
        context: Context,
    ) {
        // Configura el nombre de la postura en el TextView
        binding.nombreTextView.text = postura.nombre

        // Usa Glide para cargar la imagen de la postura en el ImageView (IVPhoto)
        Glide
            .with(context)
            .load(postura.imagen) // Carga la imagen desde la URL proporcionada en el objeto Postura
            .into(binding.IVPhoto) // Asigna la imagen cargada al ImageView

        // Configura el clic del botón circular (btnCircular).
        // Cuando el botón es presionado, se invoca el callback pasando el objectId de la postura
        binding.btnCircular.setOnClickListener {
            // Llama al callback pasando el objectId de la postura
            mostrarDialogoConfirmacionEliminarPostura(context, postura.objectId)
        }
    }

    /**
     * Muestra un cuadro de diálogo de confirmación para eliminar una postura.
     *
     * Este diálogo permite al usuario confirmar o cancelar la eliminación de una postura.
     * Si se confirma, se llama a `eliminarPosturaViewModel.eliminarPostura` para eliminar la postura,
     * y se recarga `PosturasActivity`. En caso de cancelar, el diálogo solo se cierra.
     *
     * @param context El contexto de la actividad que llama al diálogo.
     * @param objectId El id de objeto de la postura que se desea eliminar.
     */
    private fun mostrarDialogoConfirmacionEliminarPostura(
        context: Context,
        objectId: String,
    ) {
        val vinculoDialogo = DialogoConfirmacionEliminarPosturaBinding.inflate(LayoutInflater.from(context))

        val dialogo =
            AlertDialog
                .Builder(context)
                .setView(vinculoDialogo.root)
                .create()

        vinculoDialogo.botonConfirmar.setOnClickListener {
            eliminarPosturaViewModel.eliminarPostura(objectId)
            dialogo.dismiss()
        }

        vinculoDialogo.botonCancelar.setOnClickListener {
            dialogo.dismiss()
        }

        dialogo.show()
    }
}
