package com.example.salud_y_vida.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Cita (

    val id: Int? = null,
    val pacienteid: Int? = null,
    val medicoid: Int? = null,
    val fechaCita : String ? = null,
    val horaCita : String ? = null,
    val estadoCita : String? = null

) : Parcelable