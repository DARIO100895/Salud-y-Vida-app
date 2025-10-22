package com.example.salud_y_vida.data.repository

import com.example.salud_y_vida.data.model.Paciente
import com.example.salud_y_vida.data.api.ApiService
import com.example.salud_y_vida.data.deploy.RetroFitClient

class PacienteRepository (

    api : ApiService = RetroFitClient.instance
) : BaseRepository<Paciente, Int>(

    getAll = {api.getPaciente()},
    getById = {id -> api.getPaciente(id)},
    create = {paciente -> api.createPaciente(paciente)},
    update = {id,paciente -> api.updatePaciente(id,paciente)},
    delete = {id -> api.deletePaciente(id) }
)