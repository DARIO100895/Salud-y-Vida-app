package com.example.salud_y_vida

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class CitaMedicaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cita_medica)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias de los elementos
        val spinnerPaciente = findViewById<Spinner>(R.id.spinnerPaciente)
        val spinnerMedico = findViewById<Spinner>(R.id.spinnerMedico)
        val spinnerHora = findViewById<Spinner>(R.id.spinnerHora)
        val spinnerEstado = findViewById<Spinner>(R.id.spinnerEstado)
        val etFecha = findViewById<TextInputEditText>(R.id.etFecha)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        // Datos de ejemplo (en el futuro vendrán de la base de datos)
        val listaPacientes = listOf("Seleccione un paciente", "Juan Pérez", "María López", "Carlos Díaz")
        val listaMedicos = listOf("Seleccione un médico", "Dra. Ramírez", "Dr. Torres", "Dr. Fernández")
        val listaHoras = listOf("Seleccione una hora", "09:00 AM", "10:30 AM", "02:00 PM", "04:00 PM")
        val listaEstados = listOf("Seleccione un estado", "Pendiente", "Confirmada", "Cancelada")

        // Adaptadores para los spinners
        fun crearAdaptador(lista: List<String>): ArrayAdapter<String> {
            return ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lista)
        }
        spinnerPaciente.adapter = crearAdaptador(listaPacientes)
        spinnerMedico.adapter = crearAdaptador(listaMedicos)
        spinnerHora.adapter = crearAdaptador(listaHoras)
        spinnerEstado.adapter = crearAdaptador(listaEstados)

        // Selector de fecha (DatePicker)
        etFecha.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                val mes = m + 1
                val fechaSeleccionada = String.format("%02d/%02d/%d", d, mes, y)
                etFecha.setText(fechaSeleccionada)
            }, year, month, day)

            datePicker.show()
        }
        // Botón Registrar
        btnRegistrar.setOnClickListener {
            val paciente = spinnerPaciente.selectedItem.toString()
            val medico = spinnerMedico.selectedItem.toString()
            val fecha = etFecha.text.toString()
            val hora = spinnerHora.selectedItem.toString()
            val estado = spinnerEstado.selectedItem.toString()

            // Validaciones
            when {
                paciente.startsWith("Seleccione") -> {
                    Toast.makeText(this, "Seleccione un paciente", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                medico.startsWith("Seleccione") -> {
                    Toast.makeText(this, "Seleccione un médico", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                fecha.isEmpty() -> {
                    Toast.makeText(this, "Seleccione una fecha", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                hora.startsWith("Seleccione") -> {
                    Toast.makeText(this, "Seleccione una hora disponible", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                estado.startsWith("Seleccione") -> {
                    Toast.makeText(this, "Seleccione un estado", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Enviar datos a RegistroDeCitasInfoActivity
            val intent = Intent(this, CitaMedicaInfoActivity::class.java)
            intent.putExtra("elPaciente", paciente)
            intent.putExtra("elMedico", medico)
            intent.putExtra("elFecha", fecha)
            intent.putExtra("elHora", hora)
            intent.putExtra("elEstado", estado)

            startActivity(intent)
        }
    }
}