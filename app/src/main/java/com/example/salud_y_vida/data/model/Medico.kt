package com.example.salud_y_vida.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Medico (

    val id : Int? = null,
    val nombreMed : String? = null,
    val apellidoMed : String? = null,

    @SerializedName("especialMed")
    val especialidadId : Int? = null,

    val telefonoMed : String? = null,
    val estadoMed : Boolean? = null,
    val especialidad : Especialidad? = null

) : Parcelable