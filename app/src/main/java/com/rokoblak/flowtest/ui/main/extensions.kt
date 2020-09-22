package com.rokoblak.flowtest.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


fun View.onClickFlow(): Flow<View> {
    return callbackFlow {
        setOnClickListener {
            offer(it)
        }
        awaitClose { setOnClickListener(null) }
    }
}

fun CompoundButton.toggleChangedFLow(): Flow<Boolean> {
    return callbackFlow {
        setOnCheckedChangeListener { _, checked ->
            offer(checked)
        }

        awaitClose { setOnCheckedChangeListener(null) }
    }
}

fun EditText.afterTextChangedFlow(): Flow<Editable?> {
    return callbackFlow {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                offer(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        addTextChangedListener(watcher)
        awaitClose { removeTextChangedListener(watcher) }
    }
}

fun Context.registerBroadcastFlow(intentFilter: IntentFilter): Flow<Intent> {
    return callbackFlow {
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                offer(intent)
            }
        }
        registerReceiver(broadcastReceiver, intentFilter)
        awaitClose {
            unregisterReceiver(broadcastReceiver)
        }
    }
}

fun Context.networkAvailableFlow(): Flow<Boolean> {
    val flow = callbackFlow<Boolean> {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                offer(true)
            }

            override fun onLost(network: Network) {
                offer(false)
            }
        }

        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.registerNetworkCallback(NetworkRequest.Builder().run {
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            build()
        }, callback)

        awaitClose {
            manager.unregisterNetworkCallback(callback)
        }
    }

    return flow
}