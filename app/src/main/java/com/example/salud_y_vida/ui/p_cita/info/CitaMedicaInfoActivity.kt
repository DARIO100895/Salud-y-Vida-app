package com.example.salud_y_vida.ui.p_cita.info

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.salud_y_vida.R
import com.example.salud_y_vida.ui.p_cita.add.CitaMedicaActivity

class CitaMedicaInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cita_medica_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //  Referencias a los elementos
        val tvMensaje = findViewById<LinearLayout>(R.id.tvMensaje)
        val btnCrearCita = findViewById<Button>(R.id.btnCrearCita)
        val btnEditarDatos = findViewById<Button>(R.id.btnEditarDatos)
        val btnSalir = findViewById<Button>(R.id.btnSalir)
        val btnConfirmarCita = findViewById<Button>(R.id.btnConfirmarCita)

        val objIntent = intent

        val paciente = objIntent.getStringExtra("elPaciente").orEmpty()
        val medico = objIntent.getStringExtra("elMedico").orEmpty()
        val fecha = objIntent.getStringExtra("elFecha").orEmpty()
        val hora = objIntent.getStringExtra("elHora").orEmpty()
        val estado = objIntent.getStringExtra("elEstado").orEmpty()

        //  Función para crear cada fila visualmente
        fun crearFila(label: String, valor: String): LinearLayout {

            val fila = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 12, 0, 12)
            }
            val tvLabel = TextView(this).apply {
                text = "$label:"
                setTextColor(getColor(R.color.secondary_dark))
                textSize = 16f
                setTypeface(null, Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val tvValor = TextView(this).apply {
                text = valor
                setTextColor(getColor(R.color.black))
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
            }

            fila.addView(tvLabel)
            fila.addView(tvValor)
            return fila
        }

        //  Agregar filas al contenedor dinámico
        tvMensaje.addView(crearFila("Paciente", paciente))
        tvMensaje.addView(crearFila("Médico", medico))
        tvMensaje.addView(crearFila("Fecha", fecha))
        tvMensaje.addView(crearFila("Hora disponible", hora))
        tvMensaje.addView(crearFila("Estado", estado))

//  Botón Crear Cita (abre nuevamente el registro)
        btnCrearCita.setOnClickListener {
            val intent = Intent(this, CitaMedicaActivity::class.java)
            startActivity(intent)
        }

        //  Botón Editar Datos (funciona como retroceso al registro)
        btnEditarDatos.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //  Botón Salir (cierra la aplicación completamente)
        btnSalir.setOnClickListener {
            finishAffinity() // Cierra todas las actividades y sale de la app, considerar para los activitys de los demás compañeros
        }

        //  Botón Confirmar Cita (sin acción por ahora)
        btnConfirmarCita.setOnClickListener {

        }
    }
}