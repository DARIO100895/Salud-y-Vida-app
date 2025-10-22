package com.example.salud_y_vida.ui.p_paciente.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salud_y_vida.ui.p_paciente.addon.PacienteActivity
import com.example.salud_y_vida.databinding.ActivityPacienteListBinding
import com.example.salud_y_vida.ui.p_paciente.view.PacienteViewModel
import com.example.salud_y_vida.ui.dashboard.DashboardActivity
import com.example.salud_y_vida.ui.p_paciente.adapter.PacienteAdapter
import com.example.salud_y_vida.ui.p_paciente.info.PacienteInfoActivity

class PacienteListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPacienteListBinding
    private val viewModel: PacienteViewModel by viewModels()
    private lateinit var adapter: PacienteAdapter
    private var listaOriginal: List<com.example.salud_y_vida.data.model.Paciente> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPacienteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("PACIENTES", "onCreate lista")

        // Configurar RecyclerView
        adapter = PacienteAdapter(emptyList()) { paciente ->
            val intent = Intent(this, PacienteInfoActivity::class.java).apply {
                putExtra("PACIENTE", paciente)
            }
            startActivity(intent)
        }
        binding.rvPacientes.layoutManager = LinearLayoutManager(this)
        binding.rvPacientes.adapter = adapter

        // Observadores
        setupObservers()

        // Buscador
        binding.searchPaciente.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrar(newText.orEmpty())
                return true
            }
        })

        // FAB agregar
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, PacienteActivity::class.java))
        }

        // Botón Atrás
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        // Cargar datos
        Log.d("PACIENTES", "Solicitando carga...")
        viewModel.cargarPacientes()
    }

    private fun setupObservers() {
        viewModel.pacientes.observe(this) { lista ->
            Log.d("PACIENTES", "Observada lista con ${lista.size} items")
            listaOriginal = lista
            adapter.actualizar(lista)

            if (lista.isEmpty()) {
                // Mostrar mensaje si no hay datos
                Toast.makeText(this, "No hay pacientes registrados", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Log.e("PACIENTES_ERROR", it)
                Toast.makeText(this, "Error de conexión: $it", Toast.LENGTH_LONG).show()

                // Mostrar mensaje específico para cleartext
                if (it.contains("CLEARTEXT")) {
                    Toast.makeText(this, "Configuración de red necesaria", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Aquí puedes mostrar/ocultar un ProgressBar si quieres
            if (isLoading) {
                Log.d("PACIENTES", "Cargando...")
            } else {
                Log.d("PACIENTES", "Carga completada")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar datos cuando se regresa de otra actividad
        viewModel.cargarPacientes()
    }

    private fun filtrar(texto: String) {
        val filtrada = if (texto.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter {
                it.nombrePaciente?.contains(texto, true) == true ||
                        it.apellidoPaciente?.contains(texto, true) == true ||
                        it.dniPaciente?.contains(texto, true) == true
            }
        }
        adapter.actualizar(filtrada)

        // Mostrar mensaje si no hay resultados
        if (texto.isNotEmpty() && filtrada.isEmpty()) {
            Toast.makeText(this, "No se encontraron pacientes", Toast.LENGTH_SHORT).show()
        }
    }
}