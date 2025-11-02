package com.example.salud_y_vida.ui.p_horario.addon

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.salud_y_vida.data.model.Horario
import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.databinding.ActivityNuevoHorarioBinding
import com.example.salud_y_vida.ui.p_horario.view.HorarioViewModel

class NuevoHorarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNuevoHorarioBinding
    private val horarioViewModel: HorarioViewModel by viewModels()
    private lateinit var medico: Medico
    private var horasDisponibles: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoHorarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medico = intent.getParcelableExtra("MEDICO") ?: run {
            Toast.makeText(this, "Error: Médico no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val nombreCompleto = "${medico.nombreMed ?: ""} ${medico.apellidoMed ?: ""}".trim()
        binding.etNombre.setText(nombreCompleto)

        Log.d("HORARIO_DEBUG", "ID del médico recibido: ${medico.id}")

        // Cargar horarios según exista o no en BD
        if (medico.id != null && medico.id!! > 0) {
            horarioViewModel.cargarHorariosPorMedico(medico.id!!)
            observarHoras()
        } else {
            // Médico nuevo sin ID válido → carga horas locales
            Log.d("HORARIO_DEBUG", "Médico sin ID, cargando horas locales")
            cargarHorasLocales()
        }

        setupClickListeners()
    }

    private fun observarHoras() {
        horarioViewModel.horario.observe(this) { lista ->
            Log.d("HORARIO_DEBUG", "Horarios recibidos desde BD: ${lista.size}")

            if (lista.isEmpty()) {
                // Médico sin horarios → usa lista local
                Log.d("HORARIO_DEBUG", "No hay horarios en BD, cargando locales...")
                cargarHorasLocales()
                return@observe
            }

            val activos = lista.filter { it.estadoHora == true }.mapNotNull { it.horario }
            horasDisponibles = activos.sorted()

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, horasDisponibles)
            binding.spinnerPrimeraHora.adapter = adapter
            binding.spinnerSegundaHora.adapter = adapter
        }

        horarioViewModel.error.observe(this) { error ->
            error?.let {
                Log.e("HORARIO_DEBUG", "Error al cargar horarios: $it")
                Toast.makeText(this, "Error al cargar horarios: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cargarHorasLocales() {
        horasDisponibles = generarHorasLocales()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, horasDisponibles)
        binding.spinnerPrimeraHora.adapter = adapter
        binding.spinnerSegundaHora.adapter = adapter
    }

    /**
     * 🕐 Genera horas locales cada 30 minutos entre 08:00 y 17:00
     */
    private fun generarHorasLocales(): List<String> {
        val horas = mutableListOf<String>()
        var hora = 8
        var minuto = 0
        while (hora < 17) {
            val h = hora.toString().padStart(2, '0')
            val m = minuto.toString().padStart(2, '0')
            horas.add("$h:$m")
            minuto += 30
            if (minuto == 60) {
                minuto = 0
                hora++
            }
        }
        Log.d("HORARIO_DEBUG", "Horas locales generadas: $horas")
        return horas
    }

    private fun setupClickListeners() {
        binding.btnGuardar.setOnClickListener {
            val desde = binding.spinnerPrimeraHora.selectedItem?.toString()
            val hasta = binding.spinnerSegundaHora.selectedItem?.toString()

            if (desde.isNullOrEmpty() || hasta.isNullOrEmpty()) {
                Toast.makeText(this, "Seleccione ambas horas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (desde >= hasta) {
                Toast.makeText(this, "La hora de inicio debe ser menor que la hora de fin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rango = horasDisponibles.filter { it >= desde && it <= hasta }

            rango.forEach { hora ->
                val nuevoHorario = Horario(
                    medicoId = medico.id,
                    horario = hora,
                    estadoHora = true
                )
                horarioViewModel.crearHorario(nuevoHorario)
            }

            Toast.makeText(this, "Horario registrado correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
