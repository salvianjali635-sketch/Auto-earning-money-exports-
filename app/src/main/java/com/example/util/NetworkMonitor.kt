package com.example.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ConnectionType {
    WIFI, CELLULAR, NONE
}

data class NetworkStatus(
    val isConnected: Boolean = false,
    val connectionType: ConnectionType = ConnectionType.NONE,
    val transferSpeedKbps: Double = 0.0,
    val activePeers: Int = 0,
    val latencyMs: Int = 0
)

class NetworkMonitor(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _networkStatus = MutableStateFlow(getInitialStatus())
    val networkStatus: StateFlow<NetworkStatus> = _networkStatus.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            updateStatus()
        }

        override fun onLost(network: Network) {
            updateStatus()
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            updateStatus()
        }
    }

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
        updateStatus()
    }

    private fun getInitialStatus(): NetworkStatus {
        val activeNetwork = connectivityManager.activeNetwork ?: return NetworkStatus()
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return NetworkStatus()
        val isConnected = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val type = when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.CELLULAR
            else -> ConnectionType.NONE
        }
        return NetworkStatus(
            isConnected = isConnected,
            connectionType = type,
            transferSpeedKbps = if (isConnected) 280.0 else 0.0,
            activePeers = if (isConnected) 8 else 0,
            latencyMs = if (isConnected) 28 else 0
        )
    }

    fun updateStatus(isSharingActive: Boolean = true) {
        val activeNetwork = connectivityManager.activeNetwork
        val caps = activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
        val isConnected = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        val type = when {
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> ConnectionType.WIFI
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> ConnectionType.CELLULAR
            else -> ConnectionType.NONE
        }

        val speed = if (isConnected && isSharingActive) {
            (220..420).random().toDouble()
        } else 0.0

        val peers = if (isConnected && isSharingActive) (6..14).random() else 0
        val latency = if (isConnected) (22..35).random() else 0

        _networkStatus.value = NetworkStatus(
            isConnected = isConnected,
            connectionType = type,
            transferSpeedKbps = speed,
            activePeers = peers,
            latencyMs = latency
        )
    }
}
