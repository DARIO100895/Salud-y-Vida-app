package com.example.salud_y_vida.ui.p_cita.addon

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.salud_y_vida.ui.p_cita.info.CitaMedicaInfoActivity
import com.example.salud_y_vida.R
import com.example.salud_y_vida.data.model.Cita
import com.example.salud_y_vida.databinding.ActivityCitaMedicaBinding
import com.example.salud_y_vida.ui.p_cita.view.CitaViewModel
import com.example.salud_y_vida.ui.p_paciente.info.PacienteInfoActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class CitaMedicaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCitaMedicaBinding
    private val viewModel : CitaViewModel by viewModels()
    private var cita : Cita? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitaMedicaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cita = intent.getParcelableExtra("CITA")
        isEditMode = cita != null

        if(isEditMode){
            binding.textViewTitle.text = "Editar Cita"
           // llenarFormulario(cita!!)
        } else {
            binding.textViewTitle.text = "Nueva Cita"
        }
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) {success ->
            if(success) {
                Toast.makeText(this,"Cita ${if(isEditMode)"actualizado" else "creado"}correctamente",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        viewModel.citaCreada.observe(this) {cita ->
            if(cita != null && !isEditMode) {
                val intent = Intent(this, CitaMedicaInfoActivity::class.java).apply {
                    putExtra("CITA", cita)
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
        binding.btnRegistrar.setOnClickListener {
            //if(validarFormulario()) {
               // guardarCita()
            }
        }
    }

    //private fun llenarFormulario(c : Cita) {
        //binding.etNombre.setText(c.pacienteid)
    //}

//}