package com.example.kotlin.wushuapp.framework.views.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.kotlin.wushuapp.R
import com.example.kotlin.wushuapp.databinding.RegistrarEstiloBinding
import com.example.kotlin.wushuapp.framework.viewmodel.RegistrarEstiloViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ExamenActivity : ComponentActivity() {
    private lateinit var binding: RegistrarEstiloBinding
    private val viewModel: RegistrarEstiloViewModel by viewModels()
    private var imagenUri: Uri? = null
    private var imagenLogoUri: Uri? = null

    // Lanzador de actividad para seleccionar imágenes
    private val LanzadordeImagenFondo =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imagenUri = it
                cargarImagenSeleccionada(it, binding.imagen) // Cargar la imagen seleccionada en el ImageView de fondo
            }
        }

    private val LanzadordeImagenLogo =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imagenLogoUri = it
                cargarImagenSeleccionada(it, binding.imagenLogo) // Cargar la imagen seleccionada en el ImageView del logo
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inicializarBinding()
        verificarAutenticacion()
        inicializarListeners()

        viewModel.resultadoRegistro.observe(this) { resultado ->
            if (resultado.isSuccess) {
                desplegarSnackBar("Estilo registrado exitosamente.")
                // Redirigir a ConsultarEstilosActivity si es necesario
            } else {
                desplegarSnackBar("Error al registrar el estilo: ${resultado.exceptionOrNull()?.message}")
            }
        }

        viewModel.errorValidacion.observe(this) { mensajeError ->
            desplegarSnackBar(mensajeError)
        }



    }

    private fun inicializarBinding() {
        binding = RegistrarEstiloBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun inicializarListeners() {
        // Selección de imagen al hacer clic en el ImageView
        binding.imagen.setOnClickListener {
            LanzadordeImagenFondo.launch("image/*")
        }
        binding.imagenLogo.setOnClickListener{
            LanzadordeImagenLogo.launch("image/*")
        }

        // Botón para registrar estilo y subir imagen a Firebase
        binding.registrarEstiloBoton.setOnClickListener {
            registrarEstilo()
        }
        binding.botonRetroceso.setOnClickListener {
            finish()
        }
    }

    private fun verificarAutenticacion() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            currentUser.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cargarImagenPredefinida()
                } else {
                    Log.e("ActividadRegistrarEstilo", "Error al obtener el token: ${task.exception}")
                }
            }
        } else {
            Log.e("ActividadRegistrarEstilo", "Usuario no autenticado, por favor inicia sesión")
            // Aquí podrías redirigir al usuario a la pantalla de inicio de sesión
        }
    }

    private fun cargarImagenSeleccionada(uri: Uri, imageView: ImageView) {
        Glide.with(this).load(uri).into(imageView) // Cargar la imagen seleccionada en el ImageView correspondiente
    }

    private fun cargarImagenPredefinida() {
        val storage = FirebaseStorage.getInstance()
        val predefinedImageRef = storage.reference.child("default_style_image.jpg")

        predefinedImageRef.downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(this).load(uri).into(binding.imagen)
            }
            .addOnFailureListener { e ->
                Log.e("ActividadRegistrarEstilo", "Error al cargar imagen predefinida: ${e.message}")
            }
    }

    private fun registrarEstilo() {
        val nombre = binding.nombre.text.toString()
        val nombreChino = binding.nombreChino.text.toString()
        val descripcion = binding.descripcion.text.toString()

        when {
            nombre.isBlank() -> desplegarSnackBar("Ingresa un nombre")
            nombreChino.isBlank() -> desplegarSnackBar("Ingresa el nombre en chino")
            imagenUri == null -> desplegarSnackBar("Selecciona una imagen")
            imagenLogoUri == null -> desplegarSnackBar("Selecciona una imagen para el Logo")
            else -> {
                // Subir ambas imágenes
                subirImagenAFirebase(imagenUri!!) { fondoUrl ->
                    subirImagenAFirebase(imagenLogoUri!!) { logoUrl ->
                        // Crear el objeto Estilo con ambas URLs de imágenes
                        val estilo = Estilo(
                            objectoId = UUID.randomUUID().toString(),
                            nombre = nombre,
                            nombreChino = nombreChino,
                            descripcion = descripcion,
                            iconoResId = null,
                            fondoResId = null,
                            imagenBG = fondoUrl,
                            imagenLogo = logoUrl
                        )
                        viewModel.registrarEstilo(estilo)
                    }
                }
            }
        }
    }

    private fun subirImagenAFirebase(uri: Uri, onSuccess: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString()) // Devuelve la URL de descarga de la imagen
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ActividadRegistrarEstilo", "Error al subir la imagen: ${exception.message}")
            }
    }

    private fun desplegarSnackBar(mensaje: String) {
        val idColor =
            if (mensaje == "Estilo registrado exitosamente.") {
                R.color.color_exito_barra
            } else {
                com.google.android.material.R.color.design_default_color_error
            }
        configurarSnackBar(mensaje, idColor)
    }

    private fun configurarSnackBar(message: String, idColor: Int, ) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setDuration(1000)
            .apply {
            setBackgroundTint(ContextCompat.getColor(this@ExamenActivity, idColor))
            setTextColor(ContextCompat.getColor(this@ExamenActivity, android.R.color.white))

            // Añadir un callback para redirigir si el mensaje es de éxito
            addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (message == "Estilo registrado exitosamente." &&
                        (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_SWIPE)
                    ) {
                        // Cuando el Snackbar se cierra y el mensaje es de éxito, redirigir a ConsultarEstilosActivity
                        redirigirAConsultarEstilos()
                    }
                }
            })

            show()
        }
    }

    private fun redirigirAConsultarEstilos() {
        val intent = Intent(this, ConsultarEstilosActivity::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual para evitar que el usuario pueda volver con el botón de atrás.
    }

}
