package com.example.salud_y_vida.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Cita (

    val id: Int? = null,

    @SerializedName("pacienteid")
    val pacienteid: Int? = null,

    @SerializedName("medicoid")
    val medicoid: Int? = null,

    val fechaCita : String ? = null,
    val horaCita : String ? = null,
    val estadoCita : String? = null,

    val paciente : Paciente? = null,

    val medico : Medico? = null

) : Parcelable