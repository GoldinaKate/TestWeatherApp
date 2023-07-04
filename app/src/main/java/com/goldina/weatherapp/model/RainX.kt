package com.goldina.weatherapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RainX(
    @SerializedName("1h")
    val one_h: Double
):Serializable