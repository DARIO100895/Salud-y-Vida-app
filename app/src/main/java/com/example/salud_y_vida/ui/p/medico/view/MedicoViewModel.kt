package com.example.salud_y_vida.ui.p.medico.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.data.repository.MedicoRepository
import kotlinx.coroutines.launch

class MedicoViewModel : ViewModel() {

    private val medicoRepository = MedicoRepository()

    private val _medicos = MutableLiveData<List<Medico>>()
    val medicos : LiveData<List<Medico>> = _medicos

    private val _medicoCreado = MutableLiveData<Medico?>()
    val medicoCreado : LiveData<Medico?> = _medicoCreado

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess : LiveData<Boolean> = _operationSuccess

    fun cargarMedicos() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = medicoRepository.listar()
            if (result.isSuccess) {
                _medicos.value = result.getOrNull() ?: emptyList()
                _error.value = null
            } else {
                _error.value = result.exceptionOrNull() ?.message
                _medicos.value = emptyList()
            }
        } catch (e : Exception) {
            _error.value = e.message
            _medicos.value = emptyList()
        }
        _isLoading.value = false
    }

    fun crearMedico(medico : Medico) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = medicoRepository.crear(medico)
            if(result.isSuccess) {
                _medicoCreado.value = result.getOrNull()
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

    fun actualizarMedico (id : Int, medico: Medico) = viewModelScope.launch {
        _isLoading.value = true
        try{
            val result = medicoRepository.actualizar(id,medico)
            if(result.isSuccess) {
                _operationSuccess.value = true
                cargarMedicos()
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
    fun eliminarMedico(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = medicoRepository.eliminar(id)
            if (result.isSuccess) {
                _operationSuccess.value = true
                cargarMedicos()
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
