package com.example.salud_y_vida.ui.p_especialidad.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salud_y_vida.databinding.ActivityEspecialidadListBinding
import com.example.salud_y_vida.ui.dashboard.DashboardActivity
import com.example.salud_y_vida.ui.p_especialidad.adapter.EspecialidadAdapter
import com.example.salud_y_vida.ui.p_especialidad.addon.EspecialidadActivity
import com.example.salud_y_vida.ui.p_especialidad.info.EspecialidadInfoActivity
import com.example.salud_y_vida.ui.p_especialidad.view.EspecialidadViewModel


class EspecialidadListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEspecialidadListBinding
    private val viewModel : EspecialidadViewModel by viewModels()
    private lateinit var adapter : EspecialidadAdapter
    private var listaOriginal : List<com.example.salud_y_vida.data.model.Especialidad> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEspecialidadListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("ESPECIALIDAD", "onCreate lista")

        //Recycler

        adapter = EspecialidadAdapter(emptyList()) { especialidad ->
            val i = Intent(this, EspecialidadInfoActivity::class.java).apply {
                putExtra("ESPECIALIDAD", especialidad)
            }
            startActivity(i)
        }
        binding.rvEspecialidad.layoutManager = LinearLayoutManager(this)
        binding.rvEspecialidad.adapter = adapter

        //Observadores
        setupObservers()

        //Buscador
        binding.searchEspecialidad.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrar(newText.orEmpty())
                return true
            }
        })

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, EspecialidadActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        Log.d("ESPECIALIDADES", "Solicitando carga...")
        viewModel.cargarEspecialidad()
    }

    private fun setupObservers(){
        viewModel.especialidad.observe(this) { lista ->
            Log.d("ESPECIALIDADES", "Observada lista con ${lista.size} items")
            listaOriginal = lista
            adapter.actualizar(lista)

            if (lista.isEmpty()) {
                Toast.makeText(this,"No hay medicos registrados", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(this ) { error ->
            error?.let {
                Log.d("ESPECIALIDADES_ERROR", it)
                Toast.makeText(this,"Error de conexion: $it", Toast.LENGTH_LONG).show()

                if(it.contains("CLEARTEXT")) {
                    Toast.makeText(this, "Configuracion de red necesaria", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.isLoading.observe(this ) { isLoading ->
            if (isLoading) {
                Log.d("ESPECIALIDADES", "Cargando...")
            } else {
                Log.d("ESPECIALIDADES", "Carga completa")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarEspecialidad()
    }

    private fun filtrar(texto : String) {
        val filtrada = if(texto.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter {
                it.nombre?.contains(texto,true) == true
            }
        }
        adapter.actualizar(filtrada)

        if(texto.isNotEmpty() && filtrada.isEmpty()) {
            Toast.makeText(this,"No se encontraron especialidades", Toast.LENGTH_SHORT).show()
        }
    }
}