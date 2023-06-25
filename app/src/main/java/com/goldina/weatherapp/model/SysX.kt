package com.goldina.weatherapp.model

import java.io.Serializable

data class SysX(
    val country: String,
    val id: Int,
    val sunrise: Long,
    val sunset: Long,
    val type: Int
):Serializable