package com.example.kotlin.examenmoviles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kotlin.examenmoviles.ui.fragments.ExamenFragment
import com.example.kotlin.examenmoviles.R

class ExamenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.examen_activity)

        // Cargar el fragmento inicial (ExamenFragment)
        if (savedInstanceState == null) {
            loadFragment(ExamenFragment())
        }
    }

    /**
     * Carga un fragmento en el contenedor principal.
     *
     * @param fragment El fragmento a cargar.
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
