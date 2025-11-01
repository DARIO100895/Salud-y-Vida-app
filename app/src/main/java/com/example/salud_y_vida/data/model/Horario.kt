package com.example.salud_y_vida.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Horario (

    val id : Int? = null,
    @SerializedName("medicoId")
    val medicoId : Int? = null,
    val horario : String? = null,
    val estadoHora : Boolean? = null,
    val medico : Medico? = null

) : Parcelable