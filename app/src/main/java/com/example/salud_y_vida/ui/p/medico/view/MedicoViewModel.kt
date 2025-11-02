package com.example.salud_y_vida.ui.p.medico.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salud_y_vida.data.model.Especialidad
import com.example.salud_y_vida.data.model.Horario
import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.data.repository.EspecialidadRepository
import com.example.salud_y_vida.data.repository.HorarioRepository
import com.example.salud_y_vida.data.repository.MedicoRepository
import kotlinx.coroutines.launch

class MedicoViewModel : ViewModel() {

    private val medicoRepository = MedicoRepository()
    private val especialidadRepository = EspecialidadRepository()
    private val horarioRepository = HorarioRepository()

    private val _medicos = MutableLiveData<List<Medico>>()
    val medicos : LiveData<List<Medico>> = _medicos

    private val _especialidades = MutableLiveData<List<Especialidad>>()
    val especialidades : LiveData<List<Especialidad>> = _especialidades

    val horarios = MutableLiveData<List<Horario>>()
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
            // Obtener datos
            val medicosResult = medicoRepository.listar()
            val especialidadesResult = especialidadRepository.listar()

            if (medicosResult.isSuccess && especialidadesResult.isSuccess) {
                val medicos = medicosResult.getOrNull() ?: emptyList()
                val especialidades = especialidadesResult.getOrNull() ?: emptyList()

                // Mostrar nombre de Especialidad de medico
                val medicosConEspecialidad = medicos.map { medico ->
                    val especialidadEncontrada = especialidades.find { it.id == medico.especialidadId }
                    medico.copy(especialidad = especialidadEncontrada)
                }
                val medicosConHoras = medicosConEspecialidad.map { medico ->
                    val horasDelMedico = horarios.value
                        ?.filter { it.medicoId == medico.id && it.estadoHora == true }
                        ?.mapNotNull { it.horario }
                        ?.sorted()
                        ?: emptyList()
                    val resumen = if (horasDelMedico.isNotEmpty())
                        "${horasDelMedico.first()} - ${horasDelMedico.last()}"
                    else "Sin horarios"

                    medico.copy(telefonoMed = resumen)
                }
                _medicos.value = medicosConHoras
                _error.value = null
            } else {
                _error.value = medicosResult.exceptionOrNull()?.message
                    ?: especialidadesResult.exceptionOrNull()?.message
                _medicos.value = emptyList()
            }
        } catch (e: Exception) {
            _error.value = e.message
            _medicos.value = emptyList()
        }
        _isLoading.value = false
    }

    fun cargarEspecialidades() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = especialidadRepository.listar()
            if(result.isSuccess) {
                _especialidades.value = result.getOrNull()
                _error.value = null
            } else {
                _error.value = result.exceptionOrNull()?.message
                _especialidades.value = emptyList()
            }
        } catch (e : Exception) {
            _error.value = e.message
            _especialidades.value = emptyList()
        }
        _isLoading.value = false
    }

    fun crearMedico(medico : Medico) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = medicoRepository.crear(medico)
            if(result.isSuccess) {
                val creado = result.getOrNull()
                val especialidad = _especialidades.value?.find { it.id == creado?.especialidadId }
                _medicoCreado.value = creado?.copy(especialidad = especialidad)
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

    fun cargarHorariosPorMedico(idMedico: Int) = viewModelScope.launch {

        _isLoading.value = true
        try {
            val result = horarioRepository.listar()
            if (result.isSuccess) {
                val todos = result.getOrNull() ?: emptyList()
                val delMedico = todos.filter { it.medicoId == idMedico }
                horarios.value = delMedico
                _error.value = null
            } else {
                _error.value = result.exceptionOrNull()?.message
                horarios.value = emptyList()
            }
        } catch (e: Exception) {
            _error.value = e.message
            horarios.value = emptyList()
        }
        _isLoading.value = false
    }

    fun cargarMedicosConHorarios() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val medicosResult = medicoRepository.listar()
            val especialidadesResult = especialidadRepository.listar()
            val horariosResult = horarioRepository.listar()

            if (medicosResult.isSuccess && especialidadesResult.isSuccess && horariosResult.isSuccess) {
                val medicos = medicosResult.getOrNull() ?: emptyList()
                val especialidades = especialidadesResult.getOrNull() ?: emptyList()
                val horarios = horariosResult.getOrNull() ?: emptyList()

                val medicosConDatos = medicos.map { medico ->
                    val especialidad = especialidades.find { it.id == medico.especialidadId }

                    val horasDelMedico = horarios
                        .filter { it.medicoId == medico.id && it.estadoHora == true }
                        .mapNotNull { it.horario }
                        .sorted()

                    val resumen = if (horasDelMedico.isNotEmpty())
                        "${horasDelMedico.first()} AM - ${horasDelMedico.last()} PM"
                    else
                        "Sin horarios"

                    medico.copy(
                        especialidad = especialidad,
                        horarioResumen = resumen
                    )
                }
                _medicos.value = medicosConDatos
                _error.value = null
            } else {
                _error.value = medicosResult.exceptionOrNull()?.message
                    ?: especialidadesResult.exceptionOrNull()?.message
                            ?: horariosResult.exceptionOrNull()?.message
                _medicos.value = emptyList()
            }
        } catch (e: Exception) {
            _error.value = e.message
            _medicos.value = emptyList()
        }
        _isLoading.value = false
    }


    fun actualizarMedico (id : Int, medico: Medico) = viewModelScope.launch {
        _isLoading.value = true
        try{
            val result = medicoRepository.actualizar(id,medico)
            if(result.isSuccess) {
                val actualizado = result.getOrNull()
                val especialidad = _especialidades.value?.find { it.id == actualizado?.especialidadId }
                _medicoCreado.value = actualizado?.copy(especialidad = especialidad)
                _operationSuccess.value = true
                cargarMedicosConHorarios()
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
    fun eliminarMedico(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = medicoRepository.eliminar(id)
            if (result.isSuccess) {
                _operationSuccess.value = true
                cargarMedicosConHorarios()
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

    fun obtenerMedicoId(id : Int) = viewModelScope.launch{
        _isLoading.value = true
        try {
            val result = medicoRepository.obtener(id)
            if (result.isSuccess) {
                _operationSuccess.value = true
                cargarMedicosConHorarios()
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
