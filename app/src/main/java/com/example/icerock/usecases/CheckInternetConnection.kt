package com.example.icerock.usecases

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

class CheckInternetConnection @Inject constructor(var context: Context) {

    fun checkInternet(): Boolean{
        val conectMenager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiConnection = conectMenager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileConnection = conectMenager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return !(wifiConnection!!.isConnectedOrConnecting || mobileConnection!!.isConnectedOrConnecting)
    }
}