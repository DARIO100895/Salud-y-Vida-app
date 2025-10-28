package com.example.salud_y_vida.data.repository

import com.example.salud_y_vida.data.api.ApiService
import com.example.salud_y_vida.data.deploy.RetroFitClient
import com.example.salud_y_vida.data.model.Especialidad

class EspecialidadRepository (

    api : ApiService = RetroFitClient.instance
) : BaseRepository<Especialidad, Int> (

    getAll = {api.getEspecialidad()},
    getById = {id -> api.getEspecialidad(id)},
    create = {especialidad -> api.createEspecialidad(especialidad)},
    update = {id, especialidad -> api.updateEspecialidad(id, especialidad)},
    delete = {id -> api.deleteEspecialidad(id)}
)