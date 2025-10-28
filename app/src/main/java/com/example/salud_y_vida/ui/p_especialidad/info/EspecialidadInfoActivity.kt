package com.example.salud_y_vida.ui.p_especialidad.info

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.salud_y_vida.data.model.Especialidad
import com.example.salud_y_vida.databinding.ActivityEspecialidadInfoBinding
import com.example.salud_y_vida.ui.p_especialidad.addon.EspecialidadActivity
import com.example.salud_y_vida.ui.p_especialidad.view.EspecialidadViewModel

class EspecialidadInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEspecialidadInfoBinding
    private val viewModel : EspecialidadViewModel by viewModels()
    private lateinit var especialidad: Especialidad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEspecialidadInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        especialidad = intent.getParcelableExtra("ESPECIALIDAD")!!
        mostrarDatos (especialidad)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Especialidad eliminada correctamente", Toast.LENGTH_SHORT).show()
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

        binding.btnActualizar.setOnClickListener {
            val i = Intent(this, EspecialidadActivity::class.java).apply {
                putExtra("ESPECIALIDAD",especialidad)
            }
            startActivity(i)
        }

        binding.btnVolver.setOnClickListener { finish() }

        binding.btnEliminar.setOnClickListener { confirmarEliminar() }
    }

    private fun mostrarDatos(e : Especialidad) = with(binding) {
        tvId.text = "ID: ${e.id ?: "N/A"}"
        tvNombre.text = "Nombre : ${e.nombre ?: "N/A"}"
    }

    private fun confirmarEliminar() {

        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estas seguro de eliminar la especialidad : ${especialidad.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                especialidad.id?.let { id ->
                    viewModel.eliminarEspecialidad(id)
                } ?: run {
                    Toast.makeText(this, "Error: ID no valido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}