package com.example.salud_y_vida.ui.p_especialidad.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salud_y_vida.data.model.Especialidad
import com.example.salud_y_vida.data.repository.EspecialidadRepository
import kotlinx.coroutines.launch

class EspecialidadViewModel : ViewModel() {

    private val especialidadRepository = EspecialidadRepository()
    private val _especialidad = MutableLiveData<List<Especialidad>>()
    val especialidad : LiveData<List<Especialidad>> = _especialidad

    private val _especialidadCreada = MutableLiveData<Especialidad?>()
    val especialidadCreada : LiveData<Especialidad?> = _especialidadCreada
    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading
    private val _operationSuccess  = MutableLiveData<Boolean>()
    val operationSuccess : LiveData<Boolean> = _operationSuccess

    fun cargarEspecialidad() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = especialidadRepository.listar()
            if(result.isSuccess) {
                _especialidad.value = result.getOrNull() ?: emptyList()
                _error.value = null
            } else {
                _error.value = result.exceptionOrNull() ?.message
                _especialidad.value = emptyList()
            }
        } catch (e : Exception) {
            _error.value = e.message
            _especialidad.value = emptyList()
        }
        _isLoading.value = false
    }

    fun crearEspecialidad(especialidad: Especialidad) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = especialidadRepository.crear(especialidad)
            if(result.isSuccess) {
                _especialidadCreada.value = result.getOrNull()
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

    fun actualizarEspecialidad(id : Int, especialidad: Especialidad) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val  result = especialidadRepository.actualizar(id,especialidad)
            if(result.isSuccess) {
                _operationSuccess.value = true
                cargarEspecialidad()
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

    fun eliminarEspecialidad(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = especialidadRepository.eliminar(id)
            if(result.isSuccess) {
                _operationSuccess.value = true
                cargarEspecialidad()
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
    fun resetOperationSuccess() {
        _operationSuccess.value = false
    }

}