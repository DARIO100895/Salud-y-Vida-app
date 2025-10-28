package com.example.salud_y_vida.ui.p_paciente.info

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.salud_y_vida.data.model.Paciente
import com.example.salud_y_vida.databinding.ActivityPacienteInfoBinding
import com.example.salud_y_vida.ui.p_paciente.addon.PacienteActivity
import com.example.salud_y_vida.ui.p_paciente.view.PacienteViewModel

class PacienteInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPacienteInfoBinding

    private val viewModel : PacienteViewModel by viewModels()

    private lateinit var paciente : Paciente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPacienteInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recibir el objeto Paciente
        paciente = intent.getParcelableExtra("PACIENTE")!!
        mostrarDatos(paciente)

        setupObservers()
        setupClickListeners()
    }
    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Paciente eliminado correctamente", Toast.LENGTH_SHORT).show()
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

        // Botón Actualizar
        binding.btnActualizar.setOnClickListener {
            val intent = Intent(this, PacienteActivity::class.java).apply {
                putExtra("PACIENTE", paciente)
            }
            startActivity(intent)
        }

        // Botón Volver
        binding.btnVolver.setOnClickListener { finish() }

        // Botón Eliminar
        binding.btnEliminar.setOnClickListener { confirmarEliminar() }
    }

    private fun mostrarDatos(p: Paciente) = with(binding){
        tvId.text = "ID: ${p.id ?: "N/A"}"
        tvNombre.text = "Nombre: ${p.nombrePaciente ?: "N/A"}"
        tvApellido.text = "Apellido: ${p.apellidoPaciente ?: "N/A"}"
        tvDni.text = "DNI: ${p.dniPaciente ?: "N/A"}"
        tvEdad.text = "Edad: ${p.edadPaciente ?: "N/A"}"
        tvSexo.text = "Sexo: ${p.sexoPaciente ?: "N/A"}"
        tvTelefono.text = "Teléfono: ${p.telefonoPaciente ?: "N/A"}"
        tvDireccion.text = "Dirección: ${p.direccionPaciente ?: "N/A"}"
    }

    private fun confirmarEliminar() {

        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de eliminar a ${paciente.nombrePaciente} ${paciente.apellidoPaciente}?")
            .setPositiveButton("Sí") { _, _ ->
                paciente.id?.let { id ->
                    viewModel.eliminarPaciente(id)
                } ?: run {
                    Toast.makeText(this, "Error: ID no válido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }


}