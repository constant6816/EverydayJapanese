package com.constant.japaneseeveryday.util

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.RequiresPermission

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

object Reachability {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        return cm.activeNetworkInfo
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isAvailableNetwork(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected
    }
    // Private Method
}
