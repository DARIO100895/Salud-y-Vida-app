package com.example.salud_y_vida.data.api

import com.example.salud_y_vida.data.model.Cita
import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.data.model.Paciente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    //Paciente
    @GET("api/v1/pacientes")
    suspend fun getPaciente() : Response<List<Paciente>>
    @GET("api/v1/pacientes/{id}")
    suspend fun getPaciente(@Path("id") id: Int): Response<Paciente>
    @POST("api/v1/pacientes")
    suspend fun  createPaciente(@Body paciente: Paciente) : Response<Paciente>
    @PUT("api/v1/pacientes/{id}")
    suspend fun updatePaciente (@Path("id") id : Int, @Body paciente: Paciente) : Response<Paciente>
    @DELETE("api/v1/pacientes/{id}")
    suspend fun deletePaciente(@Path("id") id: Int) : Response<Void>

    //CitaMedica

    @GET("api/v1/cita")
    suspend fun getCita() : Response<List<Cita>>
    @GET("api/v1/cita/{id}")
    suspend fun getCita(@Path("id")id: Int) : Response<Cita>
    @POST("api/v1/cita")
    suspend fun createCita (@Body cita: Cita) : Response<Cita>
    @PUT("api/v1/cita/{id}")
    suspend fun updateCita (@Path("id")id: Int, @Body cita: Cita) : Response<Cita>
    @DELETE("api/v1/cita/{id}")
    suspend fun deleteCita(@Path("id")id: Int) : Response<Void>

    //Medicos

    @GET("api/v1/medicos")
    suspend fun getMedicos() : Response<List<Medico>>
    @GET("api/v1/medicos/{id}")
    suspend fun getMedicos(@Path("id") id : Int) : Response<Medico>
    @POST("api/v1/medicos")
    suspend fun createMedicos(@Body medico: Medico) : Response<Medico>
    @PUT("api/v1/medicos/{id}")
    suspend fun updateMedicos(@Path("id")id: Int, @Body medico: Medico) : Response<Medico>
    @DELETE("api/v1/medicos/{id}")
    suspend fun deleteMedicos(@Path("id") id: Int) : Response<Void>
}