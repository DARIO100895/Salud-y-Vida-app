package com.example.salud_y_vida.ui.p_horario.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salud_y_vida.data.model.Horario
import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.data.repository.HorarioRepository
import com.example.salud_y_vida.data.repository.MedicoRepository
import kotlinx.coroutines.launch

class HorarioViewModel : ViewModel() {
    private val horarioRepository = HorarioRepository()
    private val medicoRepository = MedicoRepository()

    private val _horario = MutableLiveData<List<Horario>>()
    val horario : LiveData<List<Horario>> = _horario

    private val _medico = MutableLiveData<List<Medico>>()
    val medico : LiveData<List<Medico>> = _medico

    private val _horarioCreado = MutableLiveData<Horario?>()
    val horarioCreado : LiveData<Horario?> = _horarioCreado

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess : LiveData<Boolean> = _operationSuccess

    fun cargarHorario() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val horarioResult = horarioRepository.listar()
            val medicoResult = medicoRepository.listar()

            if (horarioResult.isSuccess && medicoResult.isSuccess) {
                val horarios = horarioResult.getOrNull() ?: emptyList()
                val medicos = medicoResult.getOrNull() ?: emptyList()

                val horarioMedico = horarios.map { horario ->
                    val medicoEncontrado = medicos.find { it.id == horario.medicoId }
                    horario.copy(medico = medicoEncontrado)
                }
                _horario.value = horarioMedico
                _error.value = null
            } else {
                _error.value = horarioResult.exceptionOrNull()?.message
                    ?: medicoResult.exceptionOrNull()?.message
                _medico.value = emptyList()
            }
        } catch (e: Exception) {
            _error.value = e.message
            _horario.value = emptyList()
        }
        _isLoading.value = false

    }

    fun cargarHorariosPorMedico(idMedico: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = horarioRepository.listar()
            if (result.isSuccess) {
                val todos = result.getOrNull() ?: emptyList()
                val delMedico = todos.filter { it.medicoId == idMedico }
                _horario.value = delMedico
            } else {
                _error.value = result.exceptionOrNull()?.message
                _horario.value = emptyList()
            }
        } catch (e: Exception) {
            _error.value = e.message
            _horario.value = emptyList()
        }
        _isLoading.value = false
    }

    fun cargarTodosLosHorarios() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = horarioRepository.listar()
            if (result.isSuccess) {
                _horario.value = result.getOrNull() ?: emptyList()
            } else {
                _horario.value = emptyList()
                _error.value = result.exceptionOrNull()?.message
            }
        } catch (e: Exception) {
            _error.value = e.message
            _horario.value = emptyList()
        }
        _isLoading.value = false
    }




    fun cargarMedico() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = medicoRepository.listar()
            if(result.isSuccess) {
                _medico.value = result.getOrNull()
                _error.value = null
            } else {
                _error.value = result.exceptionOrNull()?.message
                _medico.value = emptyList()
            }
        } catch ( e : Exception) {
            _error.value = e.message
            _medico.value = emptyList()
        }
        _isLoading.value = false
    }

    fun crearHorario( horario : Horario) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = horarioRepository.crear(horario)
            if (result.isSuccess){
                val creado = result.getOrNull()
                val medico = _medico.value?.find { it.id == creado?.medicoId }
                _horarioCreado.value = creado?.copy(medico = medico)
                _operationSuccess.value = true
            } else {
                _error.value = result.exceptionOrNull()?.message
                _operationSuccess.value = false
            }
        } catch ( e : Exception) {
            _error.value = e.message
            _operationSuccess.value = false
        }
        _isLoading.value = false
    }




    fun actualizarHorario(id : Int, horario : Horario) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = horarioRepository.actualizar(id,horario)
            if(result.isSuccess) {
                val actualizado = result.getOrNull()
                val medico = _medico.value?.find { it.id == actualizado?.medicoId }
                _horarioCreado.value = actualizado?.copy(medico = medico)
                _operationSuccess.value = true
                cargarHorario()
            } else {
                _error.value = result.exceptionOrNull()?.message
                _operationSuccess.value = false
            }
        } catch ( e : Exception){
            _error.value = e.message
            _operationSuccess.value = false
        }
        _isLoading.value = false
    }

    fun eliminarHorario(id : Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = horarioRepository.eliminar(id)
            if(result.isSuccess) {
                _operationSuccess.value = true
                cargarHorario()
            } else {
                _error.value = result.exceptionOrNull()?.message
                _operationSuccess.value = false
            }
        } catch ( e : Exception) {
            _error.value = e.message
            _operationSuccess.value = false
        }
        _isLoading.value = false
    }

    fun resetOperationSuccess() {
        _operationSuccess.value = false
    }

}