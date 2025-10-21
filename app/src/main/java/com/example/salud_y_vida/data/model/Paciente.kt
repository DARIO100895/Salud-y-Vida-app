package com.example.salud_y_vida.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Paciente (

    val id: Int? = null,
    val nombrePaciente: String? = null,
    val apellidoPaciente: String? = null,
    val dniPaciente: String? = null,
    val edadPaciente: Int? = null,
    val sexoPaciente: String? = null,
    val telefonoPaciente: String? = null,
    val direccionPaciente: String? = null

) : Parcelable