package com.example.salud_y_vida.ui.p_paciente.add

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.salud_y_vida.ui.p_paciente.info.PacienteInfoActivity
import com.example.salud_y_vida.R
import com.example.salud_y_vida.data.model.Paciente
import com.example.salud_y_vida.databinding.ActivityCitaMedicaBinding
import com.example.salud_y_vida.databinding.ActivityPacienteBinding
import com.example.salud_y_vida.ui.p_paciente.view.PacienteViewModel
import com.google.android.material.textfield.TextInputEditText

class PacienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPacienteBinding

    private val viewModel : PacienteViewModel by viewModels()

    private var paciente : Paciente? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPacienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Estamos editando?

        paciente = intent.getParcelableExtra("PACIENTE")
        isEditMode = paciente != null

        if(isEditMode) {
            binding.textViewTitle.text = "Editar Paciente"
            llenarFormulario(paciente!!)
        } else {
            binding.textViewTitle.text = "Nuevo Paciente"
        }
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) {success ->
            if (success) {
                Toast.makeText(this,"Paciente ${if(isEditMode)"actualizado" else "creado"} correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        viewModel.pacienteCreado.observe(this) {paciente ->
            if(paciente != null && !isEditMode) {
                val intent = Intent(this, PacienteInfoActivity::class.java).apply {
                    putExtra("PACIENTE",paciente)
                }
                startActivity(intent)
                finish()
            }
        }

        viewModel.error.observe(this) {error ->
            error?.let {
                Toast.makeText(this,"Error: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnGuardar.setOnClickListener {
            if(validarFormulario()) {
                guardarPaciente()
            }
        }
    }

    private fun llenarFormulario(p : Paciente) {
        binding.etNombre.setText(p.nombrePaciente ?: "")
        binding.etApellido.setText(p.apellidoPaciente ?: "")
        binding.etDNI.setText(p.dniPaciente ?: "")
        binding.etEdad.setText(p.edadPaciente?.toString() ?: "")
        binding.etSexo.setText(p.sexoPaciente ?: "")
        binding.etTelefono.setText(p.telefonoPaciente ?: "")
        binding.etDireccion.setText(p.direccionPaciente ?: "")
    }

    private fun validarFormulario(): Boolean {
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val dni = binding.etDNI.text.toString().trim()

        if (nombre.isEmpty()) {
            binding.etNombre.error = "El nombre es obligatorio"
            return false
        }

        if (apellido.isEmpty()) {
            binding.etApellido.error = "El apellido es obligatorio"
            return false
        }

        if (dni.isEmpty()) {
            binding.etDNI.error = "El DNI es obligatorio"
            return false
        }

        return true
    }

    private fun guardarPaciente() {

        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val dni = binding.etDNI.text.toString().trim()
        val edad = binding.etEdad.text.toString().trim().toIntOrNull() ?: 0
        val sexo = binding.etSexo.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()

        val pacienteData = Paciente(
            id = paciente?.id,
            nombrePaciente = nombre,
            apellidoPaciente = apellido,
            dniPaciente = dni,
            edadPaciente = edad,
            sexoPaciente = sexo,
            telefonoPaciente = telefono,
            direccionPaciente = direccion
        )

        if (isEditMode) {
            viewModel.actualizarPaciente(pacienteData.id!!, pacienteData)
        } else {
            viewModel.crearPaciente(pacienteData)
        }
    }
}
