package com.example.kotlin.wushuapp.framework.adapter.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin.wushuapp.R
import com.example.kotlin.wushuapp.databinding.ItemPosturasConFormaBinding

/**
 * ViewHolder para manejar la visualización de un elemento de tipo `PosturaConForma` en un RecyclerView.
 *
 * @param binding Enlace a la vista para el ítem `PosturaConForma`.
 */
class PosturasConFormaViewHolder(private val binding: ItemPosturasConFormaBinding) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Vincula los datos de un objeto `PosturaConForma` a la vista.
     *
     * @param posturasConForma Objeto `PosturaConForma` con los datos a mostrar.
     * @param context Contexto utilizado para cargar la imagen con Glide.
     */
    fun bind(posturasConForma: PosturasConForma, context: Context) {
        // Carga la imagen de la forma utilizando Glide
        Glide.with(context)
            .load(posturasConForma.imagen)  // URL de la imagen a cargar
            .placeholder(R.mipmap.ic_launcher)  // Imagen de marcador de posición mientras carga
            .error(R.drawable.delete)  // Imagen de error si no puede cargar la URL
            .into(binding.IVPhoto)  // Asigna la imagen cargada al ImageView
    }
}