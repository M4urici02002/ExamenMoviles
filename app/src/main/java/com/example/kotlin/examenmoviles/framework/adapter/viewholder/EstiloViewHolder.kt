package com.example.kotlin.wushuapp.framework.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin.wushuapp.R

class EstiloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
    private val nombreTextView: TextView = itemView.findViewById(R.id.tvNombreEstilo)
    private val nombreChinoTextView: TextView = itemView.findViewById(R.id.tvNombreChino)
    private val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayoutEstilo)

    fun bind(estilo: Estilo) {
        nombreTextView.text = estilo.nombre
        nombreChinoTextView.text = estilo.nombreChino

        // Cargar el icono usando Glide si no es uno de los estilos principales
        if (estilo.iconoResId != null) {
            imgIcon.setImageResource(estilo.iconoResId)
        } else if (!estilo.imagenLogo.isNullOrEmpty()) {
            Glide.with(itemView.context)
                .load(estilo.imagenLogo)
                .into(imgIcon)
        }

        // Cargar el fondo del LinearLayout usando Glide si no es uno de los estilos principales
        if (estilo.fondoResId != null) {
            linearLayout.setBackgroundResource(estilo.fondoResId)
        } else if (!estilo.imagenBG.isNullOrEmpty()) {
            Glide.with(itemView.context)
                .load(estilo.imagenBG)
                .into(object : com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
                    override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                        linearLayout.background = placeholder
                    }

                    override fun onResourceReady(
                        resource: android.graphics.drawable.Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in android.graphics.drawable.Drawable>?
                    ) {
                        linearLayout.background = resource
                    }
                })
        }
    }
}