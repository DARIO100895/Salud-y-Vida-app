package com.example.salud_y_vida.ui.p_cita.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salud_y_vida.data.model.Cita
import com.example.salud_y_vida.data.repository.CitaRepository
import com.example.salud_y_vida.data.repository.PacienteRepository
import kotlinx.coroutines.launch

class CitaViewModel : ViewModel() {

    private val citaRepository = CitaRepository()
    private val pacienteRepository = PacienteRepository()
    private val _cita = MutableLiveData<List<Cita>>()
    val cita : LiveData<List<Cita>> = _cita

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _opeSuccess = MutableLiveData<Boolean>()
    val opeSuccess : LiveData<Boolean> = _opeSuccess

    fun cargarCitas() = viewModelScope.launch {

        _isLoading.value = true
        try {
            val result = citaRepository.listar()
            if(result.isSuccess) {
                val citas = result.getOrNull() ?: emptyList()

                val citasPaciente = citas.map { cita ->
                    val pacienteId = cita.pacienteid
                    if(pacienteId != null) {
                        val pacienteResult = pacienteRepository.obtener(pacienteId)
                        if(pacienteResult.isSuccess) {
                            cita.paciente = pacienteResult.getOrNull()
                        } else {
                            // Puedes loguear el error si lo deseas

                        }
                    }
                    cita
                }

                _cita.value = citasPaciente
                _error.value = null
            } else {
                _error.value = result.exceptionOrNull()?.message
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
                _opeSuccess.value = true
                cargarCitas()
            } else {
                _error.value = result.exceptionOrNull()?.message
                _opeSuccess.value = false
            }
        } catch (e : Exception) {
            _error.value = e.message
            _opeSuccess.value = false
        }
        _isLoading.value = false
    }
    fun actualizarCita(id: Int, cita : Cita) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = citaRepository.actualizar(id,cita)
            if(result.isSuccess) {
                _opeSuccess.value = true
                cargarCitas()
            } else {
                _error.value = result.exceptionOrNull()?.message
                _opeSuccess.value = false
            }
        } catch (e : Exception) {
            _error.value = e.message
            _opeSuccess.value = false
        }
        _isLoading.value = false
    }
    fun eliminarCita(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = citaRepository.eliminar(id)
            if(result.isSuccess) {
                _opeSuccess.value = true
                cargarCitas()
            } else {
                _error.value = result.exceptionOrNull()?.message
                _opeSuccess.value = false
            }
        } catch (e : Exception) {
            _error.value = e.message
            _opeSuccess.value = false
        }
        _isLoading.value = false
    }

    fun resetOpSuccess() {
        _opeSuccess.value = false
    }
}