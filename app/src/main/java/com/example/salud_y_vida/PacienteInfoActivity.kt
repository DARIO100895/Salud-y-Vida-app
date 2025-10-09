package com.example.salud_y_vida

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PacienteInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paciente_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvMensaje = findViewById<LinearLayout>(R.id.tvMensaje)
        val btnCrearCita = findViewById<Button>(R.id.btnCrearCita)
        val btnEditarDatos = findViewById<Button>(R.id.btnEditarDatos)

        val objIntent = intent

        var nombre: String = objIntent.getStringExtra("elNombre").orEmpty()
        var apellido: String = objIntent.getStringExtra("elApellido").orEmpty()
        var dni: String = objIntent.getStringExtra("elDni").orEmpty()
        var edad: String = objIntent.getStringExtra("elEdad").orEmpty()
        var sexo: String = objIntent.getStringExtra("elSexo").orEmpty()
        var telefono: String = objIntent.getStringExtra("elTelefono").orEmpty()
        var direccion: String = objIntent.getStringExtra("elDireccion").orEmpty()

        val tratamiento = if (sexo.equals("M", true) || sexo.equals("Masculino", true)) "Sr" else "Srta"

        // Función para crear cada fila visualmente
        fun crearFila(label: String, valor: String): LinearLayout {

            val fila = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 12, 0, 12)
            }

            val tvLabel = TextView(this).apply {
                text = "$label:"
                setTextColor(getColor(R.color.secondary_dark))
                textSize = 16f
                setTypeface(null, android.graphics.Typeface.BOLD)
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

        // Agregar filas al contenedor
        tvMensaje.addView(crearFila("Paciente", "$tratamiento: $nombre $apellido"))
        tvMensaje.addView(crearFila("DNI", dni))
        tvMensaje.addView(crearFila("Edad", "$edad años"))
        tvMensaje.addView(crearFila("Sexo", sexo))
        tvMensaje.addView(crearFila("Teléfono", telefono))
        tvMensaje.addView(crearFila("Dirección", direccion))

        // Botón Crear Cita
        btnCrearCita.setOnClickListener {
            //Falta implementar
        }

        // Botón Editar Datos
        btnEditarDatos.setOnClickListener {
            val intent = Intent(this, PacienteActivity::class.java)
            startActivity(intent)
        }

    }
}