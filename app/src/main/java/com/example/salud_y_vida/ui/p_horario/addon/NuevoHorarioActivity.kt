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
import com.example.salud_y_vida.ui.p.medico.view.MedicoViewModel
import com.example.salud_y_vida.ui.p_horario.view.HorarioViewModel

class NuevoHorarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNuevoHorarioBinding
    private val viewModel: MedicoViewModel by viewModels()
    private val horarioViewModel : HorarioViewModel by viewModels()
    private lateinit var medico : Medico
    private var horasDisponibles : List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoHorarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medico = intent.getParcelableExtra("MEDICO") ?: run {
            Toast.makeText(this, "Error: Médico no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return


        }
        val nombreCompleto = "${medico.nombreMed} ${medico.apellidoMed}".trim()
        binding.etNombre.setText(nombreCompleto)

        viewModel.cargarHorariosPorMedico(medico.id!!)
        observarHoras()
        setupClickListeners()

    }

        private fun observarHoras() {

            viewModel.horarios.observe(this) { lista ->

                val todos = lista.mapNotNull { it.horario }
                val activos = lista.filter { it.estadoHora == true }.mapNotNull { it.horario }

                Log.d("TodosHorarios", todos.toString())
                Log.d("HorariosActivos", activos.toString())

                horasDisponibles = activos.sorted()

                if (horasDisponibles.isEmpty()) {
                    Toast.makeText(this, "No hay horas disponibles", Toast.LENGTH_SHORT).show()
                    return@observe
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, horasDisponibles)
                binding.spinnerPrimeraHora.adapter = adapter
                binding.spinnerSegundaHora.adapter = adapter
            }
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
