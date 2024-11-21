package com.example.kotlin.wushuapp.framework.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.wushuapp.databinding.ItemPosturasConFormaBinding
import com.example.kotlin.wushuapp.framework.adapter.viewholder.PosturasConFormaViewHolder
import com.example.kotlin.wushuapp.framework.views.fragments.ImageZoomDialogFragment

/**
 * Adaptador para manejar una lista de objetos `PosturaConForma` en un RecyclerView.
 */
class PosturasConFormaAdapter : RecyclerView.Adapter<PosturasConFormaViewHolder>() {
    // Lista de datos original (sin filtrar)
    private var datos: List<PosturasConForma> = emptyList()

    // Contexto de la aplicación para cargar recursos (como imágenes)
    private lateinit var contexto: Context

    private var itemWidth: Int = 0  // Variable para almacenar el ancho del elemento

    /**
     * Establece los datos a mostrar en el adaptador y el contexto necesario.
     *
     * @param basicData Lista de posturas que se mostrarán en el RecyclerView.
     * @param contexto Contexto necesario para las operaciones que requieren acceso a recursos (como cargar imágenes).
     */
    fun establecerDatosPosturaConForma(basicData: List<PosturasConForma>, contexto: Context) {
        this.datos = basicData
        this.contexto = contexto
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }

    // Método para configurar el ancho de cada elemento
    fun setItemWidth(width: Int) {
        itemWidth = width
        notifyDataSetChanged()
    }

    /**
     * Crea un nuevo ViewHolder para una postura.
     *
     * @param parent El ViewGroup donde se insertará el nuevo elemento.
     * @param viewType El tipo de vista (en este caso no se utiliza, siempre es el mismo tipo).
     * @return Un nuevo `PosturaConFormaViewHolder` con el diseño de `ItemPosturaBinding` inflado.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosturasConFormaViewHolder {
        val binding = ItemPosturasConFormaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PosturasConFormaViewHolder(binding)
    }

    /**
     * Asocia un objeto `Postura` al ViewHolder correspondiente.
     *
     * @param holder El ViewHolder donde se mostrarán los datos.
     * @param position La posición en la lista de los datos filtrados.
     */
    override fun onBindViewHolder(holder: PosturasConFormaViewHolder, posicion: Int) {
        val item = datos[posicion]
        holder.bind(item, contexto)

        // Configurar el listener para abrir el diálogo con la imagen
        holder.itemView.setOnClickListener {
            val dialog = ImageZoomDialogFragment.newInstance(item.imagen)
            dialog.show((contexto as AppCompatActivity).supportFragmentManager, "image_zoom")
        }

        // Configura el ancho del elemento si se ha especificado
        if (itemWidth > 0) {
            holder.itemView.layoutParams.width = itemWidth
        }
    }

    override fun getItemCount(): Int {
        return datos.size
    }

}