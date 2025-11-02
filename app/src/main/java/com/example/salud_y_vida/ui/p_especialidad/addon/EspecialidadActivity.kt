package com.example.salud_y_vida.ui.p_especialidad.addon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.salud_y_vida.data.model.Especialidad
import com.example.salud_y_vida.databinding.ActivityEspecialidadBinding
import com.example.salud_y_vida.databinding.ItemEspecialidadBinding
import com.example.salud_y_vida.ui.p_especialidad.info.EspecialidadInfoActivity
import com.example.salud_y_vida.ui.p_especialidad.view.EspecialidadViewModel

class EspecialidadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEspecialidadBinding
    private val viewModel : EspecialidadViewModel by viewModels()
    private var especialidad : Especialidad? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEspecialidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Editamos?

        especialidad = intent.getParcelableExtra("ESPECIALIDAD")
        isEditMode = especialidad != null

        if(isEditMode) {
            binding.textViewTitle.text = "Editar Especialidad"
            llenarFormulario (especialidad!!)
        } else {
            binding.textViewTitle.text = "Nueva Especialidad"
        }
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) {success ->
            if (success) {
                Toast.makeText(this,"Especialidad ${if(isEditMode)"actualizado" else "creado"} correctamente",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        viewModel.especialidadCreada.observe(this) { especialidad ->
            if(especialidad != null && !isEditMode) {
                val i = Intent(this, EspecialidadInfoActivity::class.java).apply {
                    putExtra("ESPECIALIDAD", especialidad)
                }
                startActivity(i)
                finish()
            }
        }
        viewModel.error.observe(this) {error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {


        binding.btnGuardar.setOnClickListener {
            if(validarFormulario()) {
                guardarEspecialidad()
            }
        }
        binding.btnVolver.setOnClickListener { finish() }
    }

    private fun llenarFormulario(e : Especialidad) {
        binding.etNomEspecialidad.setText(e.nombre ?: "")
    }

    private fun validarFormulario() : Boolean {
        val nombre = binding.etNomEspecialidad.text.toString().trim()

        if(nombre.isEmpty()) {
            binding.etNomEspecialidad.error = "La especialidad es incorrecta"
            return false
        }

        return true
    }

    private fun guardarEspecialidad() {
        val nombre = binding.etNomEspecialidad.text.toString().trim()

        val especialData = Especialidad(
            id = especialidad?.id,
            nombre = nombre
        )

        if (isEditMode) {
            viewModel.actualizarEspecialidad(especialData.id!!, especialData)
        } else {
            viewModel.crearEspecialidad(especialData)
        }
    }

}