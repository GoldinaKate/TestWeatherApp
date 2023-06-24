package com.goldina.weatherapp.utils.network

import android.content.Context
import android.net.ConnectivityManager

class Network {

    companion object {

        fun isOnline(context: Context?): Boolean {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

    }
}