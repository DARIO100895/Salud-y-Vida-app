package com.example.salud_y_vida.ui.p.medico.info

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.databinding.ActivityMedicoInfoBinding
import com.example.salud_y_vida.ui.p.medico.addon.MedicoActivity
import com.example.salud_y_vida.ui.p.medico.view.MedicoViewModel
import com.example.salud_y_vida.ui.p_horario.addon.NuevoHorarioActivity

class MedicoInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicoInfoBinding
    private val viewModel: MedicoViewModel by viewModels()
    private lateinit var medico: Medico

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicoInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medico = intent.getParcelableExtra("MEDICO")!!
        mostrarDatos(medico)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Médico eliminado correctamente", Toast.LENGTH_SHORT).show()
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
            val intent = Intent(this, MedicoActivity::class.java).apply {
                putExtra("MEDICO", medico)
            }
            startActivity(intent)
        }
        binding.btnAgregarHorario.setOnClickListener {
            val intent = Intent(this, NuevoHorarioActivity::class.java)
            intent.putExtra("MEDICO", medico)
            startActivity(intent)
        }


        binding.btnVolver.setOnClickListener { finish() }

        binding.btnEliminar.setOnClickListener { confirmarEliminar() }
    }

    private fun mostrarDatos(m: Medico) = with(binding) {
        tvId.text = "ID: ${m.id ?: "N/A"}"
        tvNombre.text = "Nombre: ${m.nombreMed ?: "N/A"}"
        tvApellido.text = "Apellido: ${m.apellidoMed ?: "N/A"}"
        tvEspecialidad.text = "Especialidad: ${m.especialidad?.nombre ?: "Sin asignar"}"
        tvTelefono.text = "Teléfono: ${m.telefonoMed ?: "N/A"}"
        val estado = when (m.estadoMed) {
            true -> "Activo"
            false -> "Inactivo"
            null -> "Sin estado"
        }
        tvEstado.text = "Estado: $estado"

    }

    private fun confirmarEliminar() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de eliminar al Dr(a). ${medico.nombreMed} ${medico.apellidoMed}?")
            .setPositiveButton("Sí") { _, _ ->
                medico.id?.let { id ->
                    viewModel.eliminarMedico(id)
                } ?: run {
                    Toast.makeText(this, "Error: ID no válido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
