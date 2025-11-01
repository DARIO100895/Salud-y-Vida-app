package com.example.salud_y_vida.data.repository

import com.example.salud_y_vida.data.api.ApiService
import com.example.salud_y_vida.data.deploy.RetroFitClient
import com.example.salud_y_vida.data.model.Horario

class HorarioRepository (

    api : ApiService = RetroFitClient.instance

) : BaseRepository<Horario, Int>(

    getAll = {api.getHorario()},
    getById = {id -> api.getHorario(id)},
    create = {horario -> api.createHorario(horario)},
    update = {id, horario -> api.updateHorario(id,horario)},
    delete = {id -> api.deleteHorario(id)}

)