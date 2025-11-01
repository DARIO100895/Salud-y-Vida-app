package com.example.salud_y_vida.ui.p_cita.info

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.salud_y_vida.R
import com.example.salud_y_vida.data.model.Cita
import com.example.salud_y_vida.databinding.ActivityCitaMedicaInfoBinding
import com.example.salud_y_vida.ui.p_cita.addon.CitaMedicaActivity
import com.example.salud_y_vida.ui.p_cita.view.CitaViewModel

class CitaMedicaInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitaMedicaInfoBinding
    private val viewModel: CitaViewModel by viewModels()
    private lateinit var cita: Cita

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitaMedicaInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cita = intent.getParcelableExtra("CITA")!!
        mostrarDatos(cita)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Cita eliminada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnActualizar.setOnClickListener {
            val intent = Intent(this, CitaMedicaActivity::class.java).apply {
                putExtra("CITA", cita)
            }
            startActivity(intent)
        }

        binding.btnVolver.setOnClickListener { finish() }

        binding.btnEliminar.setOnClickListener { confirmarEliminar() }
    }

    private fun mostrarDatos(c: Cita) = with(binding) {
        tvId.text = "ID: ${c.id ?: "N/A"}"
        tvPaciente.text = "Paciente: ${c.paciente?.nombrePaciente ?: "Sin asignar"}"
        tvMedico.text = "Médico: ${c.medico?.apellidoMed ?: "Sin asignar"}"
        tvFecha.text = "Fecha: ${c.fechaCita ?: "N/A"}"
        tvHora.text = "Hora: ${c.horaCita ?: "N/A"}"
        tvEstado.text = "Estado: ${c.estadoCita ?: "N/A"}"
    }

    private fun confirmarEliminar() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de eliminar esta cita médica?")
            .setPositiveButton("Sí") { _, _ ->
                cita.id?.let { id ->
                    viewModel.eliminarCita(id)
                } ?: run {
                    Toast.makeText(this, "Error: ID no válido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
