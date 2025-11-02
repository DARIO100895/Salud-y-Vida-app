package com.example.salud_y_vida.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.salud_y_vida.databinding.ActivityDashboardBinding
import com.example.salud_y_vida.ui.p.medico.list.MedicoListActivity
import com.example.salud_y_vida.ui.p_cita.list.CitaListActivity
import com.example.salud_y_vida.ui.p_especialidad.list.EspecialidadListActivity
import com.example.salud_y_vida.ui.p_paciente.list.PacienteListActivity


class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()


        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón Pacientes
        binding.btnPacientes.setOnClickListener {
            startActivity(Intent(this, PacienteListActivity::class.java))
        }

        //Boton Cita Medica
        binding.btnCitas.setOnClickListener {
            startActivity(Intent(this, CitaListActivity::class.java))
        }

        //Boton Medicos
        binding.btnMedicos.setOnClickListener {
            startActivity(Intent(this, MedicoListActivity::class.java))
        }

        //Boton Especialidad
        binding.btnEspecialidades.setOnClickListener {
            startActivity(Intent(this, EspecialidadListActivity::class.java))
        }

        // Demás botones (próximamente)
        // binding.btnMedicos.setOnClickListener { ... }
    }
}
