package com.example.salud_y_vida.ui.p_cita.addon

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.example.salud_y_vida.ui.p_cita.info.CitaMedicaInfoActivity
import com.example.salud_y_vida.data.model.Cita
import com.example.salud_y_vida.data.model.Horario
import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.data.model.Paciente
import com.example.salud_y_vida.databinding.ActivityCitaMedicaBinding
import com.example.salud_y_vida.ui.p_cita.view.CitaViewModel
import java.util.Calendar

class CitaMedicaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitaMedicaBinding
    private val viewModel: CitaViewModel by viewModels()
    private var cita: Cita? = null
    private var medicos: List<Medico> = emptyList()
    private var horario : List<Horario> = emptyList()
    private var pacientes : List<Paciente> = emptyList()

    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitaMedicaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Editando?
        cita = intent.getParcelableExtra("CITA")
        isEditMode = cita != null

        if (isEditMode) {
            binding.textViewTitle.text = "Editar Cita"
            llenarFormulario(cita!!)
        } else {
            binding.textViewTitle.text = "Nueva Cita"
        }
        setupObservers()
        setupClickListeners()
        cargarMedicos()
        cargarEstados()
        cargarHorarios()
        cargarPacientes()
    }

    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(
                    this, "Cita ${if (isEditMode) "actualizada" else "creada"} correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
        viewModel.citaCreada.observe(this) { cita ->
            if (cita != null && !isEditMode) {
                val intent = Intent(this, CitaMedicaInfoActivity::class.java).apply {
                    putExtra("CITA", cita)
                }
                startActivity(intent)
                finish()
            }
        }
        viewModel.medico.observe(this) { lista ->
            medicos = lista
            val apellidos = lista.map { it.apellidoMed ?: "Sin Apellido" }
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, apellidos)
            binding.spinnerMedico.adapter = adapter

            if (isEditMode) {
                val index = apellidos.indexOf(cita?.medico?.apellidoMed)
                if (index >= 0) binding.spinnerMedico.setSelection(index)
            }
        }

            binding.spinnerMedico.setOnItemSelectedListener( object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val apellidoSeleccionado = parent?.getItemAtPosition(position).toString()
                    val medicoSeleccionado = medicos.find { it.apellidoMed == apellidoSeleccionado }
                    val horarioFiltrado = horario.filter { it.medicoId == medicoSeleccionado?.id }
                    val horas = horarioFiltrado.map { it.horario ?:"Sin hora" }

                    val adapter = ArrayAdapter(this@CitaMedicaActivity, android.R.layout.simple_spinner_dropdown_item, horas)
                    binding.spinnerHora.adapter = adapter

                    if (isEditMode) {
                        val index = horas.indexOf(cita?.horaCita)
                        if (index >= 0) binding.spinnerHora.setSelection(index)
                }
            }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
        })

        viewModel.horarios.observe(this) { lista ->
            horario = lista
        }

        viewModel.paciente.observe(this) { lista ->
            pacientes = lista
            val nombres = lista.map { it.nombrePaciente ?: "Sin nombre" }
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombres)
            binding.autoPaciente.setAdapter(adapter)

            if (isEditMode) {
                binding.autoPaciente.setText(cita?.paciente?.nombrePaciente ?: "")
            }
        }



        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRegistrar.setOnClickListener {
            if (validarFormulario()) {
                guardarCita()
            }
        }
        binding.etFecha.setOnClickListener {
            mostrarDatePicker()
        }
    }

    private fun cargarMedicos() {
        viewModel.cargarMedicos()
    }

    private fun cargarHorarios() {
        viewModel.cargarHorarios()
    }

    private fun cargarPacientes() {
        viewModel.cargarPacientes()
    }

    private fun cargarEstados() {
        val estados = listOf("Confirmada", "Pendiente", "Cancelada")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)
        binding.spinnerEstado.adapter = adapter

        if (isEditMode) {
            val index = estados.indexOf(cita?.estadoCita ?: "")
            if (index >= 0) binding.spinnerEstado.setSelection(index)
        }
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val fecha = String.format("%04d-%02d-%02d", year, month + 1, day)
                binding.etFecha.setText(fecha)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }


    private fun llenarFormulario(c: Cita) {
        binding.autoPaciente.setText(c.paciente?.nombrePaciente ?: "")
        binding.etFecha.setText(c.fechaCita ?: "")
        binding.spinnerEstado.setSelection(
            listOf("Confirmada", "Pendiente", "Cancelada").indexOf(c.estadoCita ?: "")
        )
    }

    private fun validarFormulario(): Boolean {
        val nombre = binding.autoPaciente.text.toString().trim()
        val fecha = binding.etFecha.text.toString().trim()

        if (nombre.isEmpty()) {
            binding.autoPaciente.error = "El nombre del paciente es obligatorio"
            return false
        }

        if (fecha.isEmpty()) {
            binding.etFecha.error = "La fecha es obligatoria"
            return false
        }

        if (binding.spinnerHora.selectedItem == null) {
            Toast.makeText(this, "Seleccione una hora", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.spinnerMedico.selectedItem == null) {
            Toast.makeText(this, "Seleccione un médico", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.spinnerEstado.selectedItem == null) {
            Toast.makeText(this, "Seleccione un estado", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun guardarCita() {
        val nombrePaciente = binding.autoPaciente.text.toString().trim()
        val pacienteSeleccionado = pacientes.find { it.nombrePaciente == nombrePaciente }

        val fecha = binding.etFecha.text.toString().trim()
        val hora = binding.spinnerHora.selectedItem.toString()
        val estado = binding.spinnerEstado.selectedItem.toString()
        val medicoApellido = binding.spinnerMedico.selectedItem.toString()
        val medicoSeleccionado = medicos.find { it.apellidoMed == medicoApellido }

        val citaData = Cita(
            id = cita?.id,
            pacienteid = pacienteSeleccionado?.id,
            medicoid = medicoSeleccionado?.id,
            fechaCita = fecha,
            horaCita = hora,
            estadoCita = estado
        )


        if (isEditMode) {
            viewModel.actualizarCita(citaData.id!!, citaData)
        } else {
            viewModel.crearCita(citaData)
        }
    }



}

