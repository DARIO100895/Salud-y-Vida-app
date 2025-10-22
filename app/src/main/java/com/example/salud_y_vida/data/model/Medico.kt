package com.example.salud_y_vida.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Medico (

    val id : Int? = null,
    val nombreMed : String? = null,
    val apellidoMed : String? = null,
    val especialidadId : Int? = null,
    val telefonoMed : String? = null,
    val estadoMed : String? = null

) : Parcelable