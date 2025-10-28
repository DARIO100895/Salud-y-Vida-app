package com.example.salud_y_vida.ui.p_cita.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salud_y_vida.databinding.ActivityCitaMedicaListBinding
import com.example.salud_y_vida.ui.dashboard.DashboardActivity
import com.example.salud_y_vida.ui.p_cita.adapter.CitaAdapter
import com.example.salud_y_vida.ui.p_cita.addon.CitaMedicaActivity
import com.example.salud_y_vida.ui.p_cita.info.CitaMedicaInfoActivity
import com.example.salud_y_vida.ui.p_cita.view.CitaViewModel

class CitaListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitaMedicaListBinding
    private val viewModel : CitaViewModel by viewModels()

    private lateinit var adapter: CitaAdapter
    private var listaOriginal : List<com.example.salud_y_vida.data.model.Cita> = emptyList()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCitaMedicaListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("CITA","onCreate lista")

        //RecyclerView
        adapter = CitaAdapter(emptyList()) { cita ->
            val intent = Intent(this, CitaMedicaInfoActivity::class.java).apply {
                putExtra("CITA", cita)
            }
            startActivity(intent)
        }
        binding.rvCitas.layoutManager = LinearLayoutManager(this)
        binding.rvCitas.adapter = adapter

        //Observ.
        setupObservers()

        //Buscador
        binding.searchCita.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrar(newText.orEmpty())
                return true
            }
        })
        //Agregar
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, CitaMedicaActivity::class.java))
        }

        //Atras
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        //Cargar Datos
        Log.d("CITA", "Solicitando carga..")
        viewModel.cargarCitas()
        }

    private fun setupObservers() {
        viewModel.cita.observe(this) { lista ->
            Log.d("CITA","Observada lista con ${lista.size} items")
            listaOriginal = lista
            adapter.actualizar(lista)

            if(lista.isEmpty()) {
                //Por si no hay datos
                Toast.makeText(this,"No hay citas registradas", Toast.LENGTH_SHORT).show()

            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Log.e("CITA_ERROR", it)
                Toast.makeText(this,"Error de conexion: $it", Toast.LENGTH_LONG).show()
                //Mensaje para el ClearText
                if (it.contains("CLEARTEXT")) {
                    Toast.makeText(this,"Configuracion de red necesaria", Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                Log.d("CITA","Cargando..")

            } else {
                Log.d("CITA", "Carga completa")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //Recarga datos al volver de una actividad
        viewModel.cargarCitas()
    }

    private fun filtrar(texto : String) {
        val filtrada = if(texto.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter { cita ->
                val apellidoPaciente = cita.paciente?.apellidoPaciente ?: ""
                val apellidoMedico = cita.medico?.apellidoMed ?: ""
                val estadoCita = cita.estadoCita?: ""

               apellidoPaciente.contains(texto, ignoreCase = true) == true ||
                        apellidoMedico.contains(texto,ignoreCase = true) == true ||
                       estadoCita.contains(texto, ignoreCase = true)

            }
        }
        adapter.actualizar(filtrada)


        //Si no hay resultados
        if(texto.isEmpty() && filtrada.isEmpty()) {
            Toast.makeText(this,"No se encontraron citas", Toast.LENGTH_SHORT).show()
        }
    }

}