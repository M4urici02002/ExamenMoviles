package com.example.kotlin.wushuapp.framework.adapter.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin.wushuapp.R
import com.example.kotlin.wushuapp.databinding.ItemFormaBinding

/**
 * ViewHolder para manejar la visualización de un elemento de tipo `Forma` en un RecyclerView.
 *
 * @param binding Enlace a la vista para el ítem `Forma`.
 */
class FormasViewHolder(private val binding: ItemFormaBinding) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Vincula los datos de un objeto `Forma` a la vista.
     *
     * @param forma Objeto `Forma` con los datos a mostrar.
     * @param context Contexto utilizado para cargar la imagen con Glide.
     */
    fun bind(forma: Forma, context: Context) {
        // Asigna el nombre en español de la forma al TextView correspondiente
        binding.nombreTextView.text = forma.NombreEspanol

        // Carga la imagen de la forma utilizando Glide
        Glide.with(context)
            .load(forma.imagen)  // URL de la imagen a cargar
            .placeholder(R.mipmap.ic_launcher)  // Imagen de marcador de posición mientras carga
            .into(binding.IVPhoto)  // Asigna la imagen cargada al ImageView
    }
}
