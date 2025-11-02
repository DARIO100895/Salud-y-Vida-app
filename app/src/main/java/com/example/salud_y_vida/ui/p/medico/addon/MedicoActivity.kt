package com.example.salud_y_vida.ui.p.medico.addon


import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.salud_y_vida.data.model.Especialidad
import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.databinding.ActivityMedicoBinding
import com.example.salud_y_vida.ui.p.medico.info.MedicoInfoActivity
import com.example.salud_y_vida.ui.p.medico.view.MedicoViewModel

class MedicoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicoBinding
    private val viewModel : MedicoViewModel by viewModels()
    private var medico : Medico? = null
    private var especialidades : List<Especialidad> = emptyList()
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Editando?
        medico = intent.getParcelableExtra("MEDICO")
        isEditMode = medico != null

        if(isEditMode) {
            binding.textViewTitle.text = "Editar Médico"
            llenarFormulario(medico!!)
        } else {
            binding.textViewTitle.text = "Nuevo Médico"
        }
        setupObservers()
        setupClickListeners()
        cargarEspecialidades()
        cargarEstados()
    }

    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) {success ->
            if (success) {
                Toast.makeText(this, "Medico ${if(isEditMode)"actualizado" else "creado"} correctamente",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        viewModel.medicoCreado.observe(this) {medico ->
            if(medico != null && !isEditMode) {
                val i = Intent(this, MedicoInfoActivity::class.java).apply {
                    putExtra("MEDICO", medico)
                }
                startActivity(i)
                finish()
            }
        }
        viewModel.especialidades.observe(this) { lista ->
            especialidades = lista
            val nombres = lista.map { it.nombre ?: "Sin nombre" }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombres)
            binding.spinnerEspecialidad.adapter = adapter

            if (isEditMode) {
                val index = nombres.indexOf(medico?.especialidad?.nombre)
                if (index >= 0) binding.spinnerEspecialidad.setSelection(index)
            }
        }

        viewModel.error.observe(this){error ->
            error?.let {
                Toast.makeText(this,"Error: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnGuardar.setOnClickListener {
            if(validarFormulario()) {
                guardarMedico()
            }
        }
        binding.btnVolver.setOnClickListener { finish() }
    }

    private  fun cargarEspecialidades() {
        viewModel.cargarEspecialidades()
    }


    private fun cargarEstados() {
        val estados = listOf("Activo", "Inactivo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)
        binding.spinnerEstado.adapter = adapter

        if (isEditMode) {
            val index = estados.indexOf(medico?.estadoMed ?: "")
            if (index >= 0) binding.spinnerEstado.setSelection(index)
        }
    }

    private fun llenarFormulario(m: Medico) {
        binding.etNombre.setText(m.nombreMed ?: "")
        binding.etApellido.setText(m.apellidoMed ?: "")
        binding.etTelefono.setText(m.telefonoMed ?: "")
    }

    private fun validarFormulario(): Boolean {
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val especialidadNombre = binding.spinnerEspecialidad.selectedItem?.toString() ?: ""
        val estado = binding.spinnerEstado.selectedItem?.toString() ?: ""

        if (nombre.isEmpty()) {
            binding.etNombre.error = "El nombre es obligatorio"
            return false
        }

        if (apellido.isEmpty()) {
            binding.etApellido.error = "El apellido es obligatorio"
            return false
        }

        if (telefono.isEmpty()) {
            binding.etTelefono.error = "El teléfono es obligatorio"
            return false
        }

        if (especialidadNombre.isEmpty()) {
            Toast.makeText(this, "Seleccione una especialidad", Toast.LENGTH_SHORT).show()
            return false
        }

        if (estado.isEmpty()) {
            Toast.makeText(this, "Seleccione un estado", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
    private fun guardarMedico() {
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()

        val estado = binding.spinnerEstado.selectedItem.toString()
        val estadoBoolean = estado == "Activo"

        val especialidadNombre = binding.spinnerEspecialidad.selectedItem.toString()
        val especialidadSeleccionada = especialidades.find { it.nombre == especialidadNombre }

        val medicoData = Medico(
            id = medico?.id,
            nombreMed = nombre,
            apellidoMed = apellido,
            telefonoMed = telefono,
            estadoMed = estadoBoolean,
            especialidad = especialidadSeleccionada,
            especialidadId = especialidadSeleccionada?.id
        )

        if (isEditMode) {
            viewModel.actualizarMedico(medicoData.id!!, medicoData)
        } else {
            viewModel.crearMedico(medicoData)
        }
    }
}
