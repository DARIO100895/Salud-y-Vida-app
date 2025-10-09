package com.example.salud_y_vida

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText


class PacienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paciente)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val etApellido = findViewById<TextInputEditText>(R.id.etApellido)
        val etDni = findViewById<TextInputEditText>(R.id.etDNI)
        val etEdad = findViewById<TextInputEditText>(R.id.etEdad)
        val etSexo = findViewById<TextInputEditText>(R.id.etSexo)
        val etTelefono = findViewById<TextInputEditText>(R.id.etTelefono)
        val etDireccion = findViewById<TextInputEditText>(R.id.etDireccion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        //Boton Guardar
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()
            val dni = etDni.text.toString()
            val edad = etEdad.text.toString()
            val sexo = etSexo.text.toString()
            val telefono = etTelefono.text.toString()
            val direccion = etDireccion.text.toString()

            val i = Intent(this, PacienteInfoActivity::class.java)
            i.putExtra("elNombre", nombre)
            i.putExtra("elApellido", apellido)
            i.putExtra("elDni", dni)
            i.putExtra("elEdad", edad)
            i.putExtra("elSexo", sexo)
            i.putExtra("elTelefono", telefono)
            i.putExtra("elDireccion", direccion)

            startActivity(i)
        }
    }
}