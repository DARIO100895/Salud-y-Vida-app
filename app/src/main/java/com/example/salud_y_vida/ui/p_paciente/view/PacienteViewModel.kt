package com.example.salud_y_vida.ui.p_paciente.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salud_y_vida.data.model.Paciente
import com.example.salud_y_vida.data.repository.PacienteRepository
import kotlinx.coroutines.launch

class PacienteViewModel : ViewModel() {

    private val repository = PacienteRepository()

    private val _pacientes = MutableLiveData<List<Paciente>>()
    val pacientes: LiveData<List<Paciente>> = _pacientes

    private val _pacienteCreado = MutableLiveData<Paciente?>()
    val pacienteCreado : LiveData<Paciente?> = _pacienteCreado

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> = _operationSuccess

    fun cargarPacientes() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = repository.listar()
            if (result.isSuccess) {
                _pacientes.value = result.getOrNull() ?: emptyList()
                _error.value = null
            } else {
                _error.value = result.exceptionOrNull()?.message
                _pacientes.value = emptyList()
            }
        } catch (e: Exception) {
            _error.value = e.message
            _pacientes.value = emptyList()
        }
        _isLoading.value = false
    }

    fun crearPaciente(paciente: Paciente) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = repository.crear(paciente)
            if (result.isSuccess) {
                _pacienteCreado.value = result.getOrNull()
                _operationSuccess.value = true
            } else {
                _error.value = result.exceptionOrNull()?.message
                _operationSuccess.value = false
            }
        } catch (e: Exception) {
            _error.value = e.message
            _operationSuccess.value = false
        }
        _isLoading.value = false
    }

    fun actualizarPaciente(id: Int, paciente: Paciente) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = repository.actualizar(id, paciente)
            if (result.isSuccess) {
                _operationSuccess.value = true
                cargarPacientes() // Recargar lista
            } else {
                _error.value = result.exceptionOrNull()?.message
                _operationSuccess.value = false
            }
        } catch (e: Exception) {
            _error.value = e.message
            _operationSuccess.value = false
        }
        _isLoading.value = false
    }

    fun eliminarPaciente(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = repository.eliminar(id)
            if (result.isSuccess) {
                _operationSuccess.value = true
                cargarPacientes() // Recargar lista
            } else {
                _error.value = result.exceptionOrNull()?.message
                _operationSuccess.value = false
            }
        } catch (e: Exception) {
            _error.value = e.message
            _operationSuccess.value = false
        }
        _isLoading.value = false
    }

    fun resetOperationSuccess() {
        _operationSuccess.value = false
    }
}