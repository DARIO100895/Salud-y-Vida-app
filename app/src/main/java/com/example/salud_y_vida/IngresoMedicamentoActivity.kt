package com.example.salud_y_vida

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class IngresoMedicamentoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingreso_medicamento)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etIdProveedor = findViewById<TextInputEditText>(R.id.etIdProveedor)
        val etIdMedicamento = findViewById<TextInputEditText>(R.id.etIdMedicamento)
        val etCantidad = findViewById<TextInputEditText>(R.id.etCantidad)
        val etFechaIngreso = findViewById<TextInputEditText>(R.id.etFechaIngreso)
        val etObservaciones = findViewById<TextInputEditText>(R.id.etObservaciones)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarMedicamento)

        // Click en campo ID Proveedor para abrir modal (futuro)
        etIdProveedor.setOnClickListener {
            Toast.makeText(this, "Modal de Proveedores (próximamente)", Toast.LENGTH_SHORT).show()
            // Aquí irá el código para abrir el modal de proveedores
        }

        // Click en campo ID Medicamento para abrir modal (futuro)
        etIdMedicamento.setOnClickListener {
            Toast.makeText(this, "Modal de Medicamentos (próximamente)", Toast.LENGTH_SHORT).show()
            // Aquí irá el código para abrir el modal de medicamentos
        }

        //Selector de fecha
        etFechaIngreso.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val selectorFecha = DatePickerDialog(this, { _, y, m, d ->
                val mes = m + 1
                val fechaSeleccionada = String.format("%02d/%02d/%d", d, mes, y)
                etFechaIngreso.setText(fechaSeleccionada)
            }, year, month, day)

            selectorFecha.show()
        }

        // Botón Guardar
        btnGuardar.setOnClickListener {
            val idProveedor = etIdProveedor.text.toString()
            val idMedicamento = etIdMedicamento.text.toString()
            val cantidad = etCantidad.text.toString()
            val fechaIngreso = etFechaIngreso.text.toString()
            val observaciones = etObservaciones.text.toString()

            // Validación básica
            if (idProveedor.isEmpty() || idMedicamento.isEmpty() ||
                cantidad.isEmpty() || fechaIngreso.isEmpty() || observaciones.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Enviar datos a futuroActivity

        }
    }
}