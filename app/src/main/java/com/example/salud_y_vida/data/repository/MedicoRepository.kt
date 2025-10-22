package com.example.salud_y_vida.data.repository

import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.data.api.ApiService
import com.example.salud_y_vida.data.deploy.RetroFitClient

class MedicoRepository (

    api : ApiService = RetroFitClient.instance
) : BaseRepository<Medico, Int> (

    getAll = {api.getMedicos()},
    getById = {id -> api.getMedicos(id)},
    create = {medico -> api.createMedicos(medico)},
    update = {id,medico -> api.updateMedicos(id, medico)},
    delete ={id -> api.deleteMedicos(id)}


)