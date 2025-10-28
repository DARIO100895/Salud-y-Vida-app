package com.example.salud_y_vida.ui.p_cita.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.salud_y_vida.data.model.Cita
import com.example.salud_y_vida.data.repository.CitaRepository
import com.example.salud_y_vida.data.repository.MedicoRepository
import com.example.salud_y_vida.data.repository.PacienteRepository
import kotlinx.coroutines.launch

class CitaViewModel : ViewModel() {

    private val citaRepository = CitaRepository()
    private val medicoRepository = MedicoRepository()
    private val pacienteRepository = PacienteRepository()

    private val _cita = MutableLiveData<List<Cita>>()
    val cita : LiveData<List<Cita>> = _cita

    private val _citaCreada = MutableLiveData<Cita?>()
    val citaCreada : LiveData<Cita?> = _citaCreada

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _operationSuccess = MutableLiveData<Boolean>()

    val operationSuccess : LiveData<Boolean> = _operationSuccess

    fun cargarCitas() = viewModelScope.launch {

        _isLoading.value = true
        try {
            val medicoResult = medicoRepository.listar()
            val pacienteResult = pacienteRepository.listar()
            val result = citaRepository.listar()

            if (medicoResult.isSuccess && pacienteResult.isSuccess && result.isSuccess) {
                val medicos = medicoResult.getOrNull() ?: emptyList()
                val pacientes = pacienteResult.getOrNull() ?: emptyList()
                val cita = result.getOrNull() ?: emptyList()

                val citasConDatos = cita.map { cita ->
                    val pacienteEncontrado = pacientes.find { it.id == cita.pacienteid }
                    val medicoEncontrado = medicos.find { it.id == cita.medicoid }
                    cita.copy(
                        paciente = pacienteEncontrado,
                        medico = medicoEncontrado
                    )
                }
                _cita.value = citasConDatos
                _error.value = null
            } else {
                _error.value = medicoResult.exceptionOrNull()?.message
                    ?: pacienteResult.exceptionOrNull()?.message
                            ?: result.exceptionOrNull()?.message
                _cita.value = emptyList()
            }
        } catch (e: Exception) {
            _error.value = e.message
            _cita.value = emptyList()
        }
        _isLoading.value = false
    }



    fun crearCita(cita : Cita) = viewModelScope.launch {
        _isLoading.value = true
        try{
            val result = citaRepository.crear(cita)
            if(result.isSuccess) {
                _citaCreada.value = result.getOrNull()
                _operationSuccess.value = true
            } else {
                _error.value = result.exceptionOrNull()?.message
                _operationSuccess.value = false
            }
        } catch (e : Exception) {
            _error.value = e.message
            _operationSuccess.value = false
        }
        _isLoading.value = false
    }
    fun actualizarCita(id: Int, cita : Cita) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = citaRepository.actualizar(id,cita)
            if(result.isSuccess) {
                _operationSuccess.value = true
                cargarCitas()
            } else {
                _error.value = result.exceptionOrNull()?.message
                _operationSuccess.value = false
            }
        } catch (e : Exception) {
            _error.value = e.message
            _operationSuccess.value = false
        }
        _isLoading.value = false
    }
    fun eliminarCita(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = citaRepository.eliminar(id)
            if(result.isSuccess) {
                _operationSuccess.value = true
                cargarCitas()
            } else {
                _error.value = result.exceptionOrNull()?.message
                _operationSuccess.value = false
            }
        } catch (e : Exception) {
            _error.value = e.message
            _operationSuccess.value = false
        }
        _isLoading.value = false
    }

    fun resetOpSuccess() {
        _operationSuccess.value = false
    }
}