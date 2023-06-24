package com.goldina.weatherapp.model

import java.io.Serializable

data class SysX(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
):Serializable