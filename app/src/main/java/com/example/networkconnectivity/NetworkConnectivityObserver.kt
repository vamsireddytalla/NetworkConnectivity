package com.example.networkconnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkConnectivityObserver(private val context: Context) : NetworkObserver {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observer(): Flow<NetworkObserver.Status> {
      return callbackFlow {
          val callBack = object :ConnectivityManager.NetworkCallback(){
              override fun onAvailable(network: Network) {
                  super.onAvailable(network)
                  launch { send(NetworkObserver.Status.Available) }
              }

              override fun onLosing(network: Network, maxMsToLive: Int) {
                  super.onLosing(network, maxMsToLive)
                  launch { send(NetworkObserver.Status.Losing) }
              }

              override fun onLost(network: Network) {
                  super.onLost(network)
                  launch { send(NetworkObserver.Status.Lost) }
              }

              override fun onUnavailable() {
                  super.onUnavailable()
                  launch { send(NetworkObserver.Status.UnAvailable) }
              }
          }
          connectivityManager.registerDefaultNetworkCallback(callBack)
          awaitClose {
              connectivityManager.unregisterNetworkCallback(callBack)
          }
      }.distinctUntilChanged()
    }
}