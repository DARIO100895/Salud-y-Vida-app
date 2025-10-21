package com.example.salud_y_vida.data.repository

import com.example.salud_y_vida.data.model.Cita
import com.example.salud_y_vida.data.remote.ApiService
import com.example.salud_y_vida.data.remote.RetroFitClient

class CitaRepository (
    api : ApiService = RetroFitClient.instance
) : BaseRepository <Cita,Int> (

    getAll = {api.getCita()},
    getById = {id -> api.getCita(id)},
    create = {cita -> api.createCita(cita)},
    update = {id, cita -> api.updateCita(id,cita)},
    delete = {id -> api.deleteCita(id)}

)