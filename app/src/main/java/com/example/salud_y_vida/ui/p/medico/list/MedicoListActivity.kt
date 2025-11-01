package com.example.salud_y_vida.ui.p.medico.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salud_y_vida.R
import com.example.salud_y_vida.databinding.ActivityMedicoListBinding
import com.example.salud_y_vida.ui.dashboard.DashboardActivity
import com.example.salud_y_vida.ui.p.medico.adapter.MedicoAdapter
import com.example.salud_y_vida.ui.p.medico.addon.MedicoActivity
import com.example.salud_y_vida.ui.p.medico.info.MedicoInfoActivity
import com.example.salud_y_vida.ui.p.medico.view.MedicoViewModel


class MedicoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicoListBinding
    private val viewModel : MedicoViewModel by viewModels()
    private lateinit var adapter: MedicoAdapter
    private var listaOriginal : List<com.example.salud_y_vida.data.model.Medico> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMedicoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MEDICOS", "onCreate lista")

        //Confi de Recycler

        adapter = MedicoAdapter(emptyList()) { medico ->
            val i = Intent(this, MedicoInfoActivity::class.java).apply {
                putExtra("MEDICO",medico)
            }
            startActivity(i)
        }
        binding.rvMedicos.layoutManager = LinearLayoutManager(this)
        binding.rvMedicos.adapter = adapter

        //Observadores
        setupObservers()

        //Buscador

        binding.searchMedico.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrar(newText.orEmpty())
                return true
            }
            })

        //FAB agregar
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, MedicoActivity::class.java))
        }

        //Boton Atras
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        //Cargar datos
        Log.d("MEDICOS", "Solicitando carga...")
        viewModel.cargarMedicos()
    }

    private fun setupObservers() {
        viewModel.medicos.observe(this) { lista ->
            Log.d("MEDICOS", "Observada lista con ${lista.size} items")
            listaOriginal = lista
            adapter.actualizar(lista)

            if(lista.isEmpty()) {
                Toast.makeText(this,"No hay medicos registrados", Toast.LENGTH_SHORT).show()

            }
        }

        viewModel.error.observe(this){error ->
            error?.let {
                Log.e("MEDICOS_ERROR", it)
                Toast.makeText(this,"Error de conexion: $it", Toast.LENGTH_LONG).show()

                if(it.contains("CLEARTEXT")) {
                    Toast.makeText(this,"Configuracion de red necesaria", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                Log.d("MEDICOS", "Cargando...")
            } else {
                Log.d("MEDICOS", "Carga completada")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar datos cuando se regresa de otra actividad
        viewModel.cargarMedicos()
    }

    private fun filtrar(texto : String) {
        val filtrada = if(texto.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter {
                it.nombreMed?.contains(texto, true) == true ||
                        it.apellidoMed?.contains(texto, true) == true ||
                        it.especialidadId?.toString()?.contains(texto, true) == true
            }
        }
        adapter.actualizar(filtrada)

        //Mostrar msj si no hay resultados
        if ( texto.isNotEmpty() && filtrada.isEmpty()) {
            Toast.makeText(this,"No se encontraron medicos", Toast.LENGTH_SHORT).show()
        }
    }
}