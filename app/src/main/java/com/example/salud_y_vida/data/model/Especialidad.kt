package com.example.salud_y_vida.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Especialidad (

    val id : Int? = null,
    val nombre : String? = null

) : Parcelable