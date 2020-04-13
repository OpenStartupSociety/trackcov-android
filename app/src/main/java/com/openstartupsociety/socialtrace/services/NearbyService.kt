package com.openstartupsociety.socialtrace.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.firebase.auth.FirebaseAuth
import com.openstartupsociety.socialtrace.data.local.entities.NearbyUser
import com.openstartupsociety.socialtrace.data.network.repository.NearbyUserRepository
import com.openstartupsociety.socialtrace.util.DeviceUtil
import com.openstartupsociety.socialtrace.util.JsonUtil.fromJson
import com.openstartupsociety.socialtrace.util.JsonUtil.toJson
import dagger.android.AndroidInjection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NearbyService : Service() {

    @Inject
    lateinit var nearbyUserRepository: NearbyUserRepository

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var deviceUtil: DeviceUtil

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            Log.d(TAG, "Endpoint connection: $endpointId")
            if (result.status.isSuccess) {
                sendMessage(endpointId)
            }
        }

        override fun onDisconnected(endpointId: String) {
            Log.d(TAG, "Connection disconnected: $endpointId")
        }

        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            Log.d(TAG, "Endpoint initiated: $endpointId")
            Nearby.getConnectionsClient(applicationContext)
                .acceptConnection(endpointId, payloadCallback)
        }
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            payload.asBytes()?.let {
                val text = String(it)
                saveUser(text)
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) =
            Unit
    }

    private fun saveUser(text: String) {
        Log.d(TAG, "Received text: $text")
        GlobalScope.launch {
            val nearbyUser = fromJson(text, NearbyUser::class.java)
            nearbyUser?.let {
                nearbyUserRepository.saveNearbyUser(nearbyUser)
            }
        }
    }

    private val discoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Log.d(TAG, "Endpoint found: $endpointId")
            Nearby.getConnectionsClient(applicationContext).requestConnection(
                firebaseAuth.currentUser?.uid!!,
                endpointId,
                connectionLifecycleCallback
            )
        }

        override fun onEndpointLost(p0: String) = Unit
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        startAdvertising()
        startDiscovery()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service onDestroy")
    }

    private fun startAdvertising() {
        Log.d(TAG, "Started advertising")
        Nearby.getConnectionsClient(applicationContext).startAdvertising(
            firebaseAuth.currentUser?.uid!!,
            packageName,
            connectionLifecycleCallback,
            AdvertisingOptions.Builder().setStrategy(STRATEGY).build()
        )
    }

    private fun startDiscovery() {
        Log.d(TAG, "Started discovery")
        Nearby.getConnectionsClient(applicationContext).startDiscovery(
            packageName,
            discoveryCallback,
            DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
        )
    }

    private fun sendMessage(endpointId: String) {
        val user = firebaseAuth.currentUser!!
        val nearbyUser = NearbyUser(
            deviceId = deviceUtil.getDeviceId(),
            userId = user.uid
        )
        val text = toJson(nearbyUser)
        Nearby.getConnectionsClient(applicationContext)
            .sendPayload(endpointId, Payload.fromBytes(text.toByteArray()))
    }

    companion object {
        val TAG = NearbyService::class.java.simpleName
        val STRATEGY: Strategy = Strategy.P2P_CLUSTER
    }
}