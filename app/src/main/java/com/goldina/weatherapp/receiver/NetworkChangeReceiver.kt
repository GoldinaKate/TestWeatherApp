package com.goldina.weatherapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.goldina.weatherapp.utils.network.Network
import com.goldina.weatherapp.utils.network.OnNetworkListener


class NetworkChangeReceiver(onNetworkListener: OnNetworkListener): BroadcastReceiver() {
    private var networkCallback : OnNetworkListener = onNetworkListener

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!Network.isOnline(context)) {
            networkCallback.onNetworkDisconnected()
        } else {
            networkCallback.onNetworkConnected()
        }
    }

}
