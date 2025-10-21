package com.example.salud_y_vida.ui.p_cita.add

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
import com.example.salud_y_vida.ui.p_cita.info.CitaMedicaInfoActivity
import com.example.salud_y_vida.R
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
            val paciente = spinnerPaciente.selectedItem?.toString()?.trim().orEmpty()
            val medico = spinnerMedico.selectedItem?.toString()?.trim().orEmpty()
            val fecha = etFecha.text?.toString()?.trim().orEmpty()
            val hora = spinnerHora.selectedItem?.toString()?.trim().orEmpty()
            val estado = spinnerEstado.selectedItem?.toString()?.trim().orEmpty()

            // Validaciones

            if ( paciente == "Seleccione un paciente" || paciente.isEmpty() ||
                 medico == "Seleccione un médico" || medico.isEmpty() ||
                 fecha == "Seleccione una fecha" || fecha.isEmpty() ||
                 hora == "Seleccione una hora" || hora.isEmpty()  ||
                 estado == "Seleccione un estado" || estado.isEmpty()) {

                    Toast.makeText(this, "Seleccione y complete todo los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
            }

            // Enviar datos a RegistroDeCitasInfoActivity
            val i = Intent(this, CitaMedicaInfoActivity::class.java)

            i.putExtra("elPaciente", paciente)
            i.putExtra("elMedico", medico)
            i.putExtra("elFecha", fecha)
            i.putExtra("elHora", hora)
            i.putExtra("elEstado", estado)

            Toast.makeText(this, "Cita medica guardada exitosamente", Toast.LENGTH_SHORT).show()

            startActivity(i)
        }
    }
}